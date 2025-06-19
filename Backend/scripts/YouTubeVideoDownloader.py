import os, re, ffmpeg, sys
from pytube import YouTube
from pytube.exceptions import VideoUnavailable, VideoPrivate, LiveStreamError, RegexMatchError, AgeRestrictedError
# Paths de distintos tipos de sistemas.
from pathlib import Path
from platformdirs import user_download_dir
#### FUNCION PRINCIPAL
""" Descarga un video de YouTube en el formato solicitado y lo almacena en la ruta indicada.
Args:
    videoURL (str): La URL del video de YouTube a descargar.
    videoFormat (str): El formato en el que debe ser descargado el video.
    videoDownloadsDirectory (str): La ruta de la carpeta donde se guardará el video descargado.
Raises:
    VideoUnavailable: Si el video no está disponible.
    Exception: Si se produce cualquier otro error durante la descarga.
"""
def video_downloader(videoURL, videoExtensionFormat, videoDownloadsDirectory = "/tmp/youtube_videos")-> None:
    # Si la ruta de descarga no existe la creamos, ya sea en el ordenador o en el servidor.
    if not os.path.exists(videoDownloadsDirectory): os.makedirs(videoDownloadsDirectory, exist_ok=True);
    # Validamos la URL del video recibido. Devolvemos None si la url recibida no es un video de YouTube.
    if not hasYouTubeVideoValidUrl(videoURL):
        # Forzamos que el texto debe ir a la salida estandar (stdout).
        print("ERROR: La URL proporcionada no es válida. Revise el enlace del video proporcionado.", file=sys.stdout);
        return
    # Validamos el formato del video recibido. Devovlemos None si el formato recibido no es valido.
    if not hasYouTubeVideoValidFormat(videoExtensionFormat):
        # Forzamos que el texto debe ir a la salida estandar (stdout).
        print("ERROR: El formato de descarga no es válido. Los formatos aceptados son:" \
        "aac, ogg, flac, alac, wav, aiff, dsd, pcm, mp3, mp4.", file=sys.stdout);
        return
    # Comenzamos la descarga del video de YouTube. Se capturan los errores y se fuerza que el texto debe ir a la salida de error (stderr).
    try:
        # Creamos el objeto YouTube.
        yt = YouTube(videoURL, on_progress_callback=on_progress, on_complete_callback=on_complete);
        # Validar si el video está disponible. Evitamos que los videos duren mas de 10 minutos(10 minutos son 600 segundos).
        if yt.length > 600: raise ValueError("El video es demasiado largo. Solo se permiten videos de hasta 10 minutos(600 segundos).")
        # Descargamos el video en la ruta indicada con el formato indicado. Python descarga el archivo en el servidor → se guarda físicamente.
        ruta_archivo = yt.streams.filter(progressive=True, file_extension=videoExtensionFormat).first().download(output_path=videoDownloadsDirectory)
        # Imprimimos la ruta del archivo descargado. Forzamos que el texto debe ir a la salida estándar (stdout).
        print(f"SUCCESS: {ruta_archivo}", file=sys.stdout);
    except VideoPrivate as video_private_err:
        # Se lanza cuando el video es privado.
        print(f"El video es privado. Error: {video_private_err}", file=sys.stderr);
    except LiveStreamError as live_stream_err:
        # Se lanza cuando el video es un stream/transmision en vivo.
        print(f"El video es un stream en vivo. Error: {live_stream_err}", file=sys.stderr);
    except RegexMatchError as regex_match_err:
        # Se lanza cuando no se puede analizar la página de YouTube.
        print(f"La pagina no se puede analizar. Error: {regex_match_err}", file=sys.stderr);
    except AgeRestrictedError as age_restricted_err:
        # Se lanza cuando el video tiene restricciones por edad.
        print(f"El video tiene restricciones de edad. Error: {age_restricted_err}", file=sys.stderr);
    except VideoUnavailable as e:
        # Se lanza cuando el video no puede ser accedido (borrado, restricción geográfica, etc.).
        print(f"El video no esta disponible. Error: {e}", file=sys.stderr);
    except Exception as e:
        # Se lanza cuando se produce cualquier otro error durante la descarga
        # (error de red, error al acceder a yt.length, fallo en la descarga, etc.).
        print(f"Error durante la descarga. Error: {e}", file=sys.stderr);
#### FUNCIONES AUXILIARES
# Función para validar la URL de YouTube.
def hasYouTubeVideoValidUrl(url)-> bool:
    # Si el video no tiene un patron que coincida con los que tienen los propios videos de youtube indica que no es un video de youtube y por lo tanto no se descargara.
    if (url.startswith("https://www.youtube.com/") or url.startswith("https://youtu.be/")): return True
    return False
##
# Función para validar el formato del video mediante una constante interna/privada.
_FORMATS = { "aac", "aiff", "alac", "flac", "wav", "ogg", "dsd", "pcm", "mp3", "mp4" }
def hasYouTubeVideoValidFormat(format="wav")-> bool:
    if format in _FORMATS: return True
    return False
##
# Funcion de YouTube que se llama automáticamente cada vez que se descarga un fragmento (chunk) del video. Sirve para mostrar el progreso en tiempo real.
def on_progress(stream, chunk, bytes_remaining):
    total_size = stream.filesize  # Tamaño total del video en bytes
    bytes_downloaded = total_size - bytes_remaining  # Bytes ya descargados
    percent = (bytes_downloaded / total_size) * 100  # Porcentaje de progreso
##
# Funcion de YouTube que se llama automáticamente cuando el video termina de descargarse completamente.
def on_complete(stream, file_path): 
    value = f"Descarga completa: {stream.title}. Guardado en: {file_path}"
    #print(value)
#### ENTRADA PRINCIPAL
if __name__ == "__main__": video_downloader(sys.argv[1], sys.argv[2]);