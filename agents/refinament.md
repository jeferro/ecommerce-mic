---
alwaysApply: false
description: Refinamiento de historias de usuario utilizando una descripción funcional
---

# Reglas de Refinamiento de Historias de Usuario

Sigue estas reglas estrictamente cuando el usuario te pida refinar una HU.

## Reglas Generales

*   Solo realizar el refinamiento de la parte de **back-end**.
*   La información de la parte de **front-end** debe utilizarse para determinar los atributos necesarios en el modelo, además de los datos de entrada y salida en las peticiones.
*   Tener en cuenta la regla para el desarrollo `agents/development.md`.
*   Por cada HU, crear un archivo MD en el mismo directorio que la descripción funcional. El nombre será el mismo que el HU con el prefijo `hu-`.
*   Los nombres de clases, métodos, atributos y paquetes deben estar en ingles.

## Estructura del Documento

### Título

*   Utilizar el nombre de la HU como **título** del archivo (notación H1), que es un texto muy breve describiendo la funcionalidad.

### Descripción

*   Explicar breve y sencillamente la funcionalidad, escrita desde la pespectiva de la persona que desea esa nueva capacidad.

*   Utilizar el formato tabla:

|          |                                     |
|----------|-------------------------------------|
| Cómo     | Tipo de usuario                     |
| Quiero   | Realizar una acción / funcionalidad |
| Para     | Obtener un beneficio o valor        |

### Modelo de Datos

*   Añadir una sección por cada **clase del modelo** como título el nombre de la clase
*   Por cada sección, añadir una tabla con los atributos de la clase con las columnas:
  *   Nombre: nombre del atributo. Utilizar camelCase y empezar en minúsculas
  *   Tipo: Tipo del atributo
    *   Usar tipos Java.
    *   Si es una paramétrica, usar `ParametricValue`.
    *   Si es booleano o Si/No, usar `boolean`.
    *   Si es un enumerado o paramétrica, añadir valores con el formato: `nombre(valor)`
  *   Obligatorio: 
    *   Añadir valor "Sí" si el atributo es obligatorio
    *   En otro caso, dejar la columna vacía
  *   Validaciones: listado con las validaciones de integridad dentro de la clase:
    *   Valores por defecto
    *   Rangos
    *   Regex para formatos (email, teléfono, etc.)
    *   Número máximo de elementos
    *   Si tiener, añadir valores por defecto
    *   Si no tiene validaciones, dejar la columna vacía
  *   Autocalculado: 
    *   Añadir valor "Sí" si el atributo es autocalculado con otros atributos de la clase
    *   Indicar la fórmula para calcular el atributo
    *   Si no es autocalculado, dejar la columna vacía
*   Incluir cambios a realizar en los modelos, NO repetir estado actual

### Contratos

#### REST
*   Listado para indicar la siguiente información:
  *   URL
  *   Query parameters: listado de filtros
  *   Body: listado de atributos del modelo que se envian en el cuerpo
  *   Response: indicar si devolver el modelo completo, sino especificar los nombres de los atributos a devolver 
*   Incluir nombre del atributo, tipo de dato y validaciones de formato.
*   No incluir descripción de los atributos.
*   Si el atributo es una clase, añadir un lista con sus atributos


### Cambios Principales
* Añadir un listado con pasos a seguir para realizar la tarea:
*   **Contratos**: archivos a modificar en el directorio `apis`.
*   **Caso de uso**: nombre del caso de uso con:
    *   Validaciones entre agregados (ej: el cliente debe existir).
    *   Validaciones de negocio NO resuelto en el modelo de datos
*   **Migración de datos**: si es necesaria.
*   **Integraciones**: nuevas integraciones requeridas.
*   **Proceso despliegue especial**: si aplica.

*   Si algún valor no aplica, indicar "N/A".


