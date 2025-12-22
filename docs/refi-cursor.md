# Refinamiento

## Reglas

* Solo realizar el refinamiento de la parte de back.

* La información de la parte de front utilizarla para determinar los atributos necesarios en el modelo, además de los datos de entrada y salida en las peticiones.

* Tener en cuenta la guía de desarrollo `docs/development-guide.md`

* Por cada HU, crear un archivo MD en el mismo directorio que la descripción funcional. El nombre será el mismo que el HU con el prefijo "hu-".

* Utlizar el nombre de la HU como **título** del archivo (notación H1), que es un texto muy breve describiendo la funcionalidad.

* Añadir sección de **descripción**:

  * Debe explicar **breve y sencillamente** la funcionalidad, escrita desde la perspectiva de la persona que desea esa nueva capacidad (usualmente un usuario o cliente del sistema).

  * El formato a utilizar es:
  |          |                                     |
  |----------|-------------------------------------|
  | Cómo     | Tipo de usuario                     |
  | Quiero   | Realizar una acción / funcionalidad |
  | Para     | Obtener un beneficio o valor        |

  * Un ejemplo:

  |          |                                           |
  |----------|-------------------------------------------|
  | Cómo     | usuario registrado                        |
  | Quiero   | recuperar mi contraseña                   |
  | Para     | volver a acceder a mi cuenta si la olvido |

* La HU NO tendrá ninguna sección de **criterios de aceptación**, porque dichos criterios se definirán en el modelo, definición contrato y pasos a implementar.

* Añadir una sección **modelo de datos** con sus cambios o actualizaciones:

  * Utilizar como formato un listado donde se indica el nombre del atributo, el tipo de dato y sus validaciones.
  
  * En cuanto a los nombres:
    * Deben ir en negrita
    * Empiezan en minúsculas y utilizan camel case
  
  * Sobre el tipo de dato:
    * Irá después del nombre entre paréntesis.
    * Utilizar los tipos de datos de Java.
    * Si el atributo es una clase específica del proyecto, añadir el nombre de la clase como tipo
    * Si el atributo es una paramétrica, utilizar el tipo "ParametricValue"
    * Si es un dato booleano o con valores Si/No, utilizar el tipo "boolean".
  
  * Para las validaciones:
    * Especificar las validaciones para la integridad de los datos dentro del modelo (agregado): dato obligatorio, dato por defecto, fecha no puede ser anterior a ahora, valor tiene que ser positivo, valor es un porcentaje entonces el valor tiene que ser entre 0 y 100, etc
    * Las validaciones de integridad entre agregados NO deben aparecer en esta sección, por ejemplo: al crear una factura que el cliente asociado exista, validación del dato usando los valores de una paramétrica, comprobaciones de permisos, etc
    * Añadir un "*" al nombre del campo si es obligatorio.
    * Añadir validaciones de formato: email, número de teléfono, documento de identidad, etc. Especificar la expresión regular a utilizar en Java si es necesario.
    * Si es un enumerado, se indica los posibles valores con su nombre usando el formato: `nombre1(valor1)`, `nombre2(valor2)`,
    * Si es una paramétrica, se indica los posibles valores con su nombre siguiendo el mismo formato que con los enumerados.
    * Si es una clase, añadir un subnivel por cada atributo que la compone.
  
  * Alguno de los atributos pueden ser autocalculados:
    * Añadir (autocalculado) después del tipo
    * Indicar como se calcula, por ejemplo en el caso del precio total: price * amount * (1 - discount)

  * Un ejemplo:
    * id*: (String) 
    * address*: (Address)
      * roadType*: (ParametricValue)
      * roadType: (String)
      * number: (String) número positivo
    
  * Si la HU es para crear el modelo y clases DTO en infraestructura, añadir todos los atributos y validaciones de integridad de los datos dentro del modelo.

  * Si la HU no es para crear el modelo, incluir solo los cambios a realizar en el modelo para realizar la funcionalidad.

* Existirá una sección **Contratos** definiendo los cambios en las APIs rest, grapql, etc
  * Si es necesario crear un nuevo servicio, añadir todos la información necesaria
  * Si es necesario modificar un servicio existente, añadir únicamente los cambios
  * Para los contratos REST, añadir un listado con:
    * URL: del nuevo servicio a definir o del servicio a modificar
    * Query parameters: parámetros de la url utilizados en la petición, por ejemplo, los filtros en los servicios de búsqueda.
    * Body: enumeración de todos los atributos de entrada, utilizando el mismo formato que en el modelo de datos. 
    * Response: indicando si se devuelve toda la información del agregado. Si no indicar los atributos a devolver utilizando el mismo formato que en el modelo de datos

* Añadir sección "Cambios principales" donde se explica brevemente los pasos a seguir para realizar la tarea:
  * El formato será un listado con los siguientes valores:
    * Contratos: indicar los contratos a modificar en el directorio `apis`
    * Caso de uso: nombre del caso de uso a crear o modificar, donde:
      * Añadir listado con las validaciones de datos entre agregados (no incluidas en el modelado): al dar de alta una factura el cliente debe existir, validación de datos paramétricos, etc
      * Añadir validaciones de la descripción funcional no incluidas en modelado: la factura NO debe estar pagada para poder modificarla
    * Migración de datos: describir si se tiene que crear una migración para actualizar los datos existentes en la base de datos.
    * Integraciones: indicar si se debe añadir una nueva integración
    * Proceso despliegue especial: indicar pasos especialiales en el proceso de despligue
  * Si algún valor del listado anterior no tiene valor, indicar "N/A" 
  * Añadir otros cambios importantes en la hora del desarrollo
