# Esquema para el modelo de datos de medicamentos en Art Health.
**@autor Juan Gilabert Lopez**
*@typedef {Object} HealthMedicamentSchema*
*@property {string} codigoNacionalMedicamento* Código nacional único del medicamento (obligatorio).
*@property {string} nombreMedicamento* Nombre del medicamento (obligatorio).
*@property {Object} viaAdministracionMedicamento* Información sobre la vía de administración del medicamento (obligatorio).
*@property {string} viaAdministracionMedicamento.forma* Forma de administración del medicamento (ej. cápsula, jarabe) (obligatorio).
*@property {string} viaAdministracionMedicamento.tipo* Tipo de administración del medicamento (ej. oral, intravenoso) (obligatorio).
*@property {number} cantidadTotalCajaMedicamento* Cantidad total de unidades en la caja del medicamento (obligatorio).
*@property {Date} fechaCaducidadMedicamento* Fecha de caducidad del medicamento (obligatorio).
*@property {string} [notasMedicamento]* Notas adicionales sobre el medicamento (opcional).

# 💊 Ficha de Medicamento - Art Health
**Código nacional:** 567890  
**Nombre del medicamento:** Salbutamol 100 mcg  
**Vía de administración:**  
- Forma: inhalación  
- Tipo: aerosol  
**Cantidad total en caja:** 200 unidades  
**Fecha de caducidad:** 20 de junio de 2025  
**Notas adicionales:** Revisar las indicaciones del médico.