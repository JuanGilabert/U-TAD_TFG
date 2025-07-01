// Modulos locales como el servicio de email.
import {} from '../services/email/emailGenerator.mjs';
//// Exportamos la función afterUserRegister encargada de enviar el correo de confirmación al ususario cuando se registra.
export async function afterUserRegister(userEmail) {
    try {
        await sendConfirmationEmail(userEmail, user.verificationToken);
        console.log(`Correo de confirmación enviado a ${user.email}`);
    } catch (err) {
        console.error('Error al enviar el correo de confirmación:', err);
    }
}