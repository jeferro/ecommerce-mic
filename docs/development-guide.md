# Development Guide

Esta guía detalla la estructura de un proyecto combinando el **Diseño Orientado al Dominio (DDD)** con la **Arquitectura Hexagonal (Ports & Adapters).** El objetivo es lograr un código **desacoplado**, **testable** y **centrado en la lógica de negocio (Dominio)**.

# Estructura general

La estructura raíz refleja los **Bounded Contexts** del sistema y los **Aggregate Roots** que lo conforman:

| **Nivel** | **Paquete Principal** | **Propósito (DDD)** |
| --- | --- | --- |
| **1er Nivel** | `domain` | Paquete base de la aplicación, contiene todo el código de la aplicación |
| **2do Nivel** | `subdomain` | Representa el Bounded Context a desarrollo o con el que nos integramos |
| 2do Nivel | `subdomain_views` | Para separar las vista de la lógica de negocio, las vistas o proyecciones de un bounded context de la aplicación se encapsularán en un paquete a parte |
| **3er Nivel** | `aggregate_root o projection` | Aggregage Root o Projection del Bounded Context |

Dentro de cada paquete de un Aggregage Root, representamos las diferentes capas:

| **Paquete** | **Propósito (DDD)** |
| --- | --- |
| `domain` | Encapsula toda la lógica de negocio |
| `application` | Contiene los casos de uso y su orquestación |
| `infrastructure` | Encapsula las integraciones del sistema, tanto, de entrada (rest api, grpc api, etc) como de salida (base de datos, rest client, grpc api, etc) |

Un ejemplo de la estructura de un proyecto puede ser:

```java
src/
	ecommerce/
		invoices/
      invoices/
        application/
        domain/
        infrastructure/
    products/
      products/
        application/
        domain/
        infrastructure/
```

# Domain

Este es el corazón de la aplicación, incluyendo toda la lógica de negocio (Dominio).

**No debe tener dependencias** de frameworks o tecnología externa, excluyendo:

- **Lombok:** eliminar la redundancia en el código al automatizar la generación de código repetitivo o "boilerplate”.
- **Apache Commons:** utilizados para el desarrollo Java.

La estructura debe ser:

| **Paquete Interno** | **Contenido** | **Responsabilidad (DDD)** |
| --- | --- | --- |
| `models` | **Clases de Aggregage Roots**, **Entities y Value Objects**. | Define el estado y comportamiento de la lógica de negocio. |
| `repositories` | **Interfaces** de Repositorio (Puertos de Salida) | Define cómo se accede y guarda el Agregado (Contrato para la infraestructura). |
| `services` | **Servicios de Dominio** | Lógica que involucra múltiples Agregados o Entidades, pero que no pertenece a un Agregado específico. |
| `exceptions` | **Excepciones de Dominio** | Control de errores específicos del negocio. |
| `utils` | **Clases de utilidades** |  |

```java
src/
	ecommerce/
		support/
			parametrics/
				domain/
					models/
						Parametric
						ParametricId
					services/
						ParametricFinder
		invoices/
      invoices/
        domain/
	        models/
		        InvoiceId
		        Invoice
		        InvoiceLine
	        events/
		        InvoiceLineAdded
		        InvoiceLineRemoved
	        exceptions/
		        InvoiceNotFoundException
		        InvoiceNotAllowedException
		        InvoiceAlreadyPaidException
	        repositories/
		        InvoicesRepository
```

## Identifiers

Todo entity y aggregate root debe tener un identificador que extienda de la clase Identifier. Donde existen ya implementadas ya las clases StringIdentifier y UUIDIdentifier para estos casos.

Los identifiers se deben alojar en el paquete `models`

Un ejemplo:

```java
public class InvoiceId extendds UUIDIdentifier {

	public InvoiceId(String value) {
    super(value);
  }
  
  public static InvoiceId createOf() {
	  var value = generateUuid();
	  
	  return new InvoiceId(value);
  }
}
```

## Models

Para crear cual modelo (agregado, entidad o value object) se debe realizar siempre mediante un **factory method** en lugar de usar un constructor. El propósito es dotar de más semántica al código, ya que podemos poner un nombre significativo a cada factory method, y utilizar los constructores únicamente en los mappers o mothers.

```java
@AllArgsConstructor
public class Invoice extends AggregateRoot<InvoiceId> {

	protected LocalDate date;
	
	protected Set<InvoiceLine> lines;
	
	protected BigDecimal total;
	
	protected boolean paid;
	
	public static Invoice createEmpty(LocalDate date) {
		var invoiceId = InvoiceId.createOf();
		
		var lines = new ArrayList<>();
		var initialTotal = BigDecimal.valueOf(0);
		
		var invoice = new Invoice(invoiceId, lines, initialTotal, false);
		
		var event = InvoiceCreated.createOf(invoice);
		invoice.record(invoice);
		
		return invoice;
	}
}
```

Además, por cada caso de uso debe existir un **método público de acción** que contenga la lógica de negocio a ejecutar en ese agregado, entidad o value object en el caso de uso.

Por lo que todo cambio en los datos un agregado, entidad o value object debe ser realizado por un método acción específico y por tanto, estas clases no debería tener casi nunca métodos setters. Permitiendo así mantener la integridad de los datos dentro del propio agregado:

```java
@AllArgsConstructor
public class Invoice extends AggregateRoot<InvoiceId> {

	// Other code...
	
	public void addLine(Product product, long amount) {
		ensureInvoiceIsNotPaid();
		
		ValueValidationUtils.ensureRequired(product, "product");
		
		var line = InvoiceLine.createOf(product, amount, 0);
		
		lines.add(line);
		
		total = total.add(line.getTotal());
		
		var invoiceLineAdded = InvoiceLineAdded.create(this, productId);
		record(invoiceLineAdded);
	}
	
	public void removeLine(ProductId productId) {
		ensureInvoiceIsNotPaid();
			
		ValueValidationUtils.ensureRequired(productId, "productId");
			
		var line = lines.stream()
			.filter(line -> line.belongsTo(productId))
			.findFirst()
			.orElse(null);
			
		if(line == null){
			throw InvoiceLineNotFound.createOf(id, productId);
		}
			
		lines.remove(line);
		
		total = total.substract(line.getTotal());
		
		var invoiceLineRemoved = InvoiceLineRemoved.create(this, productId);
		record(invoiceLineRemoved);
	}
	
	public void paid() {
		ensureInvoiceIsNotPaid();
		
		paid = true;
	}
	
	private void ensureInvoiceIsNotPaid() {
		if(paid) {
			throw InvoiceAlreadyPaidException.createOf();
		}
	}
}
```

Además, como se puede ver en los ejemplos anteriores, tanto **los factory method como los métodos  públicos son responsables de validar los datos de entrada**: datos requeridos, validación de precio mayor que cero, validación de porcentaje entre cero y cien, etc

```java
@AllArgsConstructor
public class InvoiceLine {

	protected ProductId productId;
	
	protected float price;
	
	protected int amount;
	
	protected float discount;
	
	public static InvoiceLine createOf(Product product, int amount, float discount) {
		ValueValidationUtils.ensureRequired(product, "productId");
		ValueValidationUtils.ensurePossitive(amount, "amount");
		ValueValidationUtils.ensurePercentage(discount, "discount");
		
		return new InvoiceLine(product.getId(), 
			product.getPrice(), 
			amount, 
			discount);
	}
	
	// Other code...
}
```

Y también validar las reglas de negocio: en el código de ejemplo de una clase Invoice, se puede observar el método privado ensureInvoiceIsNotPaid, que comprueba que no se puede realizar ningún cambio en una factura que ya esté pagada. El acuerdo en estas validaciones que se puede reutilizar y que lanzan una excepción de dominio, es **crear un método privado que empiece con la palabra “ensure”**.

Los agregados, entidades y value object también encapsularán la lógica de negocio de cálculos o similares:

```java
public class InvoiceLine {
	
	// Other code...
	
	public BigDecimal getTotal() {
		BigDecimal bdPrice = BigDecimal.valueOf(price);
		BigDecimal bdAmount = BigDecimal.valueOf(amount);
		BigDecimal bdDiscount = BigDecimal.valueOf(discount);
		
		BigDecimal discountFactor = BigDecimal.ONE.subtract(bdDiscount);
		BigDecimal baseTotal = bdPrice.multiply(bdAmount);
		
		BigDecimal finalTotal = baseTotal.multiply(discountFactor);
		return finalTotal.setScale(2, RoundingMode.HALF_UP);
	}
	
	public boolean belongsTo(ProductId productId) {
		return this.productId.equals(productId);
	}
}
```

### Value Objects

Representan valores con validaciones y/o lógica de negocio propia.

```java
public class Email extends ValueObject {

	private static final String EMAIL_REGEX = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

	private final String value;
	
	public Email(String value) {
		this.value = value;
		
		ensureFormat();
	}
	
	private static void ensureFormat() {
		var matcher = PATTERN.matcher(email);
    
    if(!matcher.matches()) {
	    throw ValueValidationException.createOf("Incorrect email format: " + this.value);
    }
	}
}
```

En el caso de los Value Object, y excepcionalmente, las validaciones se realizan en el constructor en lugar de un named constructor. Esto es debido a que se pueden utilizar en los params de un caso de uso, y así validamos los datos de entrada.

### Mothers

Para la realización de los test se utilizará el patrón Mother que consiste en tener factories de los objetos (agregados, entidades o value objects) y utilizarlos en los tests del resto de clases. Simplificando así el resto de tests.

En estas clases NO debemos usar los factory method de las entidades sino su constructor, así evitamos que se añadan los eventos de creación al agregado.

```java
public class InvoiceMother {
	public static Invoice createUnpaidEmptyInvoice() {
		var date = LocalDate.of(2025, 01, 01);
		var lines = new ArrayList();
		var total = 0;
		var paid = false;
		
		return new Invoice(date);
	}
	
	public static Invoice createPaidInvoice() {
		var tomatoLine = new InvoiceLine(ProductMother.createTomato(),
			3.50,
			2,
			0);
		
		var lines = new ArrayList();
		lines.add(tomatoLines);
			
		var date = LocalDate.of(2025, 01, 01);
		var total = 0;
		var paid = false;
		
		return new Invoice(date);
	}
	
protected ProductId productId;
	
	protected float price;
	
	protected int amount;
	
	protected float discount;
}
```

## Events

La realización de acciones en los agregados generará eventos necesarios para la realización de las tareas asíncronas en otros agregados y módulos.

Como acuerdo, el nombre de la clase debe representar que la acción ya se ha realizado y por tanto debería ser en pasado.

```java
public class InvoiceLineAdded extends InvoiceEvent {
	
	private final ProductId productId;
	
	public InvoiceLineAdded(Invoice entity, ProductId productId) {
    super(entity);
    
    this.productId = productId;
  }

  public static InvoiceLineAdded create(Invoice invoice, ProductId productId) {
    return new InvoiceLineAdded(invoice, productId);
  }
}
```

## Exceptions

Las excepciones lanzadas desde el dominio puede ser de dos tipos:

- **Genéricas:** casos donde los clientes pueden realizar ellos mismos las validaciones (estáticas como datos obligatorios, formato de los datos, etc) o donde no necesitan realizar un flujo alternativo. Para este tipo de excepciones se usarán códigos genéricos de errores.
- **Específicas:** casos donde los clientes no pueden realizar las validaciones (dinámicas como comprobar si un dato existe en la base de datos, estado de un entidad, etc) y/o necesitan realizar un flujo alternativo. Para este tipo de excepciones se usarán códigos específicos de errores.

En el sistema existen las siguientes excepciones ya listas para ser usadas:

- **ForbiddenException**: para casos donde el usuario no tenga permisos para realizar la operación solicitada:

```java
public class InvoiceNotAllowedException extends ForbiddenException {
	
	protected InvoiceNotAllowedException(String code, String message) {
    super(code, "Invoice not allowed", message);
  }

  public static InvoiceNotAllowedException createOf(UserId userId, InvoiceId invoiceId) {
    return new InvoiceNotAllowedException(
        INVOICE_NOT_FOUND,  
        "Invoice " + invoiceId + " not allowed by user " + userId);
  }
}
```

- **IncorrectVersionException**: cuando un usuario no esté actualizando la última versión de una entidad o agregado.
- **NotFoundException**: la entidad o agregado indicado no existe. Al ser una clase abstracta, se debe extender creando una excepción por cada entidad o agregado.

```java
public class InvoiceNotFoundException extends NotFoundException {
	
	protected InvoiceNotFoundException(String code, String message) {
    super(code, "Invoice not found", message);
  }

  public static InvoiceNotFoundException createOf(InvoiceId invoiceId) {
    return new InvoiceNotFoundException(
        INVOICE_NOT_FOUND,  
        "Invoice " + invoiceId + " not found");
  }
}
```

- **ValueValidationException**: encapsula errores de validaciones genéricos o estáticos: formato incorrecto en los datos, dato obligatorio, etc. También se puede extender con excepciones nueva para validaciones de negocio específicas como validaciones de negocio (factura ya pagada y por tanto no se puede modificar, etc).

```java
public class InvoiceAlreadyPaidException extends ValueValidationException {
	
	protected InvoiceAlreadyPaidException(String code, String message) {
    super(code, "Invoice already paid", message);
  }

  public static InvoiceAlreadyPaidException createOf(InvoiceId invoiceId) {
    return new InvoiceAlreadyPaidException(
        INVOICE_ALREADY_EXISTS,  
        "Invoice " + invoiceId + " already paid");
  }
}
```

Como se puede ver, en las excepciones tanto propias o genéricas el título es siempre el mismo, y lo que se personaliza es el código y mensaje que se emite.

## Repositories

Interfaces (puertos de salida) para el acceso a los datos almacenados de los agregados, por lo que debería existir un repositorio por cada agregado como máximo. Nunca debería existir un repositorio para una entidad.

Además, se acuerda los nombres “find” y “save” para recuperar y actualizar los datos. Donde el método “save” crea o actualiza el registro y devuelve el registro almacenado. Además, los métodos que devuelve una entidad, debería devolver un objeto Optional:

```java
public interface InvoiceRepository {

	Invoice save(Invoice invoice);
	
	Optional<Invoice> findById(InvoiceId invoiceId);
	
	default Invoice findByIdOrError(InvoiceId invoiceId) {
		return findById(invoiceId)
			.orElseThrow(() -> InvoiceNotFoundException.createOf(invoiceId));
	}
	
	void delete(Invoice invoice);
	
	void deleteAll(List<Invoice> invoices);
	
	List<Invoice> findAllByCriteria(InvoiceCriteria criteria);
	
	long countByCriteria(InvoiceCriteria criteria);
}
```

### Criteria

Se acuerda el uso del **patrón Criteria** para la realización de las búsquedas de varios registros en el almacenamiento de dato. Con esto se persigue reducir el número de métodos del repositorio.

```java
@RequiredArgsConstructor
public class InvoiceCriteria extends DomainCriteria {

	private final ProductId productId;
	
	public boolean hasProductId() {
		return productId != null;
	}
	
}
```

### InMemory

Para el test del resto de clase que tienen como dependencia un repository (casos de uso y servicios), deberemos crear en el paquete de test una implementación del repository utilizando solo datos en memory

```java
public class InvoicesInMemoryRepository extends InMemoryRepository<Invoice, InvoiceId>
    implements InvoicesRepository {

  public InvoicesInMemoryRepository() {
    var unpaidEmptyInvoice = InvoiceMother.createUnpaidEmptyInvoice();
    data.put(unpaidEmptyInvoice.getId(), unpaidEmptyInvoice);

    var paidInvoice = ReviewMother.createPaidInvoice();
    data.put(paidInvoice.getId(), paidInvoice);
  }

  @Override
  public List<Invoice> findAll(InvoiceCriteria criteria) {
    var entities = data.values().stream()
            .filter(invoice -> matchCriteria(invoice, criteria))
            .sorted((i1, i2) -> compare(i1, i2, criteria))
            .toList();

    return paginateEntities(entities, criteria);
  }

  @Override
  public long count(ReviewCriteria criteria) {
    return findAll(InvoiceCriteria).size();
  }

  @Override
  public void deleteAll(List<Invoice> invoices) {
    invoices.forEach(this::delete);
  }

  private boolean matchCriteria(Invoice invoice, InvoiceCriteria criteria) {
    return matchProductId(invoice, criteria);
  }

  private boolean matchProductId(Invoice invoice, InvoiceCriteria criteria) {
    return criteria.hasProductId()
	    && invoice.applyToProductId(criteria.getProductId());
  }

  private int compare(Invoice i1, Invoice i2, InvoiceCriteria criteria) {
    var order = criteria.getOrder();

    if(order == null){
      return -1;
    }

    return switch (criteria.getOrder()) {
	  default -> -1;
	};
  }
}

```

## Services

Los servicios pueden ser i**nterfaces o puertos de salida,** al igual que los repositorios, se define una interfaz de dominio para la utilización de sistemas externos. Un ejemplo puede ser la interfaz EmailNotifier que permite enviar notificaciones por email desde el dominio, y que se implementará en la capa de infraestructura.

```java
public interface EmailNotifier {
	void notity(String destination, String content);
}
```

Además también existirán servicios con la **implementación de la lógica de negocio que no pertenezca a ningún agregado en concreto** (al consumir el último producto en stock, se debe dar de baja el producto como disponible) **o lógica compleja** que es difícil encapsular en las entidades de dominio (asignar a una categoría teniendo en cuenta su stock, etiquetas que definen, y otra lógica compleja).

### Mock

Para el desarrollo de los test de los casos de uso que dependan de un servicio cuya implementación se encuentra en infraestructura, debemos crear una clase moqueando su comportamiento:

```java
@RequiredArgsConstructor
public class EmailNotification extends ValueObject {
	private final String destination;
	
	private final String content;
	
	
}

@Getter
public class EmailMockNotifier implements EmailNotifier {

	private List<EmailNotification> notifications
	
	public EmailMockNotifier() {
		notifications = new ArrayList();
	}
	
	@Override
	public void notity(String destination, String content) {
		var notification = new EmailNotification(destination, content);
		
		notifications.add(notification);
	}
	
}
```

En este caso el Mock almacena únicamente las notificaciones enviadas, para así desde el caso de uso poder validar que se están enviando correctamente (assert).

## Projections

Son representaciones de datos optimizada para la lectura que pueden ser:

- **Proyecciones de bounded context externos**: donde se proyecta una entidad de una aplicación externa para poder consultar su información principalmente.

    ```java
    @AllArgsConstructor
    public class Parametric extends Projection<ParametricId> {
    	
    	private final LocalizedField name;
    
      private final List<ParametricValue> values;
      
      public boolean validate(ParametricValueId parametricValueId) {
        if (containsValue(parametricValueId)) {
          return true;
        }
    
        throw ParametricValueNotFoundException.createOf(this, parametricValueId);
      }
      
      private boolean containsValue(ParametricValueId parametricValueId) {
        return values.stream()
            .noneMatch(parametricValue -> parametricValue.hasSameId(parametricValueId));
      }
    }
    ```

- **Proyecciones del bounded context:** simplicación de alguna entidad (devolver un listado de objectos más sencillo en la búsqueda de una entidad) o combinación de datos (factura con comentarios incluidos en el mismo objecto).

    ```java
    @AllArgsConstructor
    public class ExtendInvoice extends Projection<InvoiceId> {
    	
    	private final Invoice invoice;
    	
    	private final Client client;
    }
    ```


Como son entidades de sólo lectura, no dispondrán de repositorio, sino que en su lugar, se recuperarán mediante un servicio denominado Finder que representa que únicamente se podrán consultar información y nunca modificar. La estructura será similar a los repositorio, pero únicamente tendrá métodos de consulta:

```java
public interface ExtendInvoiceFinder {
	
	Optional<ExtendInvoice> findById(InvoiceId invoiceId);
	
	default ExtendInvoice findByIdOrError(InvoiceId invoiceId) {
		return findById(invoiceId)
			.orElseThrow(() -> ExtendInvoiceNotFoundException.createOf(invoiceId));
	}
	
	List<ExtendInvoice> findAllByCriteria(ExtendInvoiceCriteria criteria);
	
	long countByCriteria(ExtendInvoiceCriteria criteria);
}
```

El resto de capas será similar a la de un agregado, manteniendo la misma paquetización:

```java
src/
	ecommerce/
		invoices/
		extend_invoices/
			application/
				params/
					SearchExtendInvoicesParams
				SearchExtendInvoicesUseCase
			domain/
				models/
					ExtendInvoice
				exceptions/
					ExtendInvoiceNotFoundException
				services/
					ExtendInvoiceFinder
			infrastructure/
				postgres/
					dtos/
						ExtendInvoicePostgresDTO
					mappers/
						ExtendInvoicePostgresMapper
					ExtendInvoicePostgresFinder
```

### Summary

Una excepción será los objectos Summary usado para recuperar una representación más sencilla en los servicio de búsqueda de las entidades:

```java
@AllArgsConstructor
public class InvoiceSummary extends Projection<InvoiceId> {
	
	private final LocalDate date;
	
	private final BigDecimal total;
}
```

Donde por simplificación no será necesario crear un nuevo módulo, sino que se creará en el mismo módulo de la entidad principal:

```java
src/
	ecommerce/
		invoices/
			application/
				params/
					SearchInvoicesParams
				SearchInvoicesUseCase
			domain/
				models/
					Invoice
					InvoiceId
					InvoiceSummary
				exceptions/
					InvoiceNotFoundException
				repositories/
					InvoiceRepository
```

Y únicamente se modificará el Repository usando esta entidad:

```java
public interface InvoiceRepository {

	Invoice save(Invoice invoice);
	
	Optional<Invoice> findById(InvoiceId invoiceId);
	
	default Invoice findByIdOrError(InvoiceId invoiceId) {
		return findById(invoiceId)
			.orElseThrow(() -> InvoiceNotFoundException.createOf(invoiceId));
	}
	
	void delete(Invoice invoice);
	
	void deleteAll(List<Invoice> invoices);
	
	List<InvoiceSummary> findAllByCriteria(InvoiceCriteria criteria);
	
	long countByCriteria(InvoiceCriteria criteria);
}
```

### Composición de Datos

Si la vista es por una composición de datos, dicha composición se tiene que realizar en la infraestructura. Por ejemplo:

- Composición rest: si debemos realizar varias llamadas para la composición de datos, podemos crear un servicio Compositor en el paquete `rest_client` que realice las llamadas preferiblemente en paralelo y componga la vista.
- Composición en base de datos: la implementación del Finder en infraestructura realizará la consulta compleja (realizando join de la información de los agregados) o consulta sobre una vista en la base de datos.

# Application

Esta capa encapsula los casos de uso de la aplicación, que orquesta el flujo de trabajo utilizando los elementos de la capa de dominio. Donde se disponen de la siguiente manera:

| **Paquete Interno** | **Contenido** |
| --- | --- |
| `params` | Parámetros de los casos de uso |
| `<base>` | Casos de uso |

Un ejemplo sería:

```java
src/
	ecommerce/
		invoices/
      invoices/
        application/
	        params/
		        AddInvoiceLineParams
		        RemoveInvoiceLineParams
	        AddInvoiceLineUseCase
	        RemoveInvoiceLineUseCase
```

## Params

Cada caso de uso dispondrá de una clase Params específica que encapsula los datos de entrada necesarios para el caso de uso.

```java
@Getter
public class AddInvoiceLineParams extends Params<Invoice> {

  private final InvoiceId invoiceId;
  
  private final ProductId productId;
  
  private final long amount;

  public CreateEmptyInvoiceParams(InvoiceId invoiceId, ProductId productId, 
	  long amount) {
    super();

    ValueValidator.isRequired(invoiceId, "invoiceId");
    ValueValidator.isRequired(productId, "productId");

    this.invoiceId = invoiceId;
    this.productId = productId;
    this.amount = amount;
  }
}

```

En el constructor de cada params, se realizará la **validación de los datos requeridos**. El resto de validaciones de los datos de entrada se entienden como reglas de negocio, que deberían estar en la capa de dominio.

**Los atributos de estas clases deben ser tipos básicos o value objects**, nunca agregados o entidades. Ya que los agregados deberían ser creados en el flujo del caso de uso. Si se necesitáramos una clase específica para la entrada de datos, podríamos utilizar una clase con el sufijo Input:

```java
@AllArgsConstructor
public class InvoiceLineInput {
	
	private final ProductId productId;
	
	private final long amount;
}
```

Al utilizar clases de dominio en lugar de DTOs propios de la capa de aplicación, evitamos tener que hacer la conversión de datos en los casos de uso.

## Casos de Uso

Los casos de uso serán los que orquestán el flujo de trabajo con la responsabilidad de:

- Mantener la transaccionalidad de la operación si es necesario.
- Validar que las entidades indicadas existan, manteniendo así la integridad de los datos referenciados entre los agregados.
- Realizar las validaciones entre diferentes agregados, por ejemplo, para cerrar un grupo todas las entidades que lo componen deben estar cerrados también.
- Llamar a los métodos de acción de los agregados.
- Publicación de eventos generados en la acción.

```java
@Component
@RequiredArgsConstructor
public class AddInvoiceLineUseCase extends UseCase<AddInvoiceLineParams, Review> {

  private final InvoicesRepository invoicesRepository;
  
  private final ProductsRepository productsRepostiroy;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(USER);
  }

  @Override
  public Review execute(Auth auth, CreateReviewParams params) {
	  var product = productsRepository.findByIdOrError(params.getProductId());
	  
    var invoice = invoicesRepository.findByIdOrError(params.getInvoiceId());
    
    invoice.

		invoicesRepository.save(invoice);

    eventBus.sendAll(invoice);
    
    return invoice;
  }
}

```

<aside>
💡

Como nota general un caso de uso debería llamar a un único método de un agregado, haciendo asíncronamente el resto de operaciones.

Pero en ocasiones por reglas de negocio, se deben actualizar sincronamente varios agregados al mismo tiempo. Lo que implica que un caso de uso llamará a varias acciones de varios agregados: operaciones masivas, operaciones síincronas entre diferentes agregados.

</aside>

### Idempotencia

Los casos de uso de actualización deberían ser idempotente en la medida de lo posible, tanto los que se ejecutan sincronamente (peticiones rest) como asíncronamente (consumo de eventos). Para esto:

- Ejecutados sincronamente: utilizar un código de versionado en los agregados para verificar que siempre se realice la operación con la última versión y no con una anterior. Además este código también soluciona problemas de concurrencia a la hora de modificar la información.
- Ejecutados asíncronamente: almacenar la fecha de la última actualización para no realizar ninguna operación si la nueva fecha de actualización es anterior.

Pero estas estrategias no son obligatorias y por tanto, se debe estudiar cada caso de uso para determinar si tiene sentido aplicarlas o no, o si se tiene que aplicar otra.

### Operaciones masivas

Para operaciones masiva de actualización de datos se deben:

- Dividir el trabajo a realizar en lotes y realizarlo paralelamente
- Evitar el uso de transacciones globales en toda la operación para mejor el rendimiento y evitar errores. Se podría utilizar transaccionalidad a nivel de lote.
- Registrar errores producidos, sin bloquear el procesamiento del resto de lotes

```java
@Component
@RequiredArgsConstructor
public class DeleteAllInvoicesUseCase extends UseCase<DeleteAllInvoicesParams, Void> {

  private static final int PAGE_SIZE = 100;

  private final InvoicesRepository invoicesRepository;

  private final EventBus eventBus;

  @Override
  public Set<String> getMandatoryUserRoles() {
    return Set.of(ADMIN);
  }

  @Override
  public Void execute(Auth auth, DeleteAllInvoicesParams params) {
    var totalPages = calculateTotalPages();

    IntStream.range(0, totalPages)
        .parallel()
        .forEach(pageNumber -> deleteInvoicesOnPage(params, pageNumber));

    return null;
  }

  private int calculateTotalPages() {
    var allInvoicesCriteria = InvoiceCriteria.all();

    var totalInvoices = invoicesRepository.count(allInvoicesCriteria);

	return PageUtils.calculateTotalPages(totalInvoices, PAGE_SIZE);
  }

  private void deleteInvoicesOnPage(DeleteAllInvoicesParams params, 
	  int pageNumber) {
	  try {
	    var byPageCriteria = InvoiceCriteria.byPage(pageNumber,
	        PAGE_SIZE);
	
	    var invoices = invoicesRepository.findAll(byPageCriteria);
	
	    invoices.forEach(Invoice::deleteBySystem);
	
	    invoicesRepository.deleteAll(invoices);
	
	    eventBus.sendAll(invoices);
	  }
	  catch(Exception cause) {
		  logger.error("Error delete page " + pageNumber, cause);
	  }
  }
}

```

### Casos de uso de administración

Para los casos de uso que no son de negocio pero que se necesitan para la administración de la aplicación, se pueden definir a nivel de:

- application: para los casos de uso con lógica de negocio: limpieza o modificación de datos de negocio, etc
- infrastructure: para los casos de uso puramente técnicos, por ejemplo: limpieza de cachés, actualización de configuraciones, etc

### Testing

A la hora de realizar los test de los casos de uso se acordó seguir una estrategia de test solidarios, donde no se deben moquear todos las clases o interfaces de las que depende. Sino:

- Si la dependencia es un repositorio, deberemos utilizar su implementación InMemory.
- Si la dependencia es un servicio de dominio con implementación en el propio dominio, debemos utilizar la propia clase del servicio.
- Si la dependencia es un servicio de dominio con implementación en la infraestructura, deberemos crear un servicio mocker o similar simulando su comportamiento.

Se persigue entonces poder testear los casos de uso como una caja negra, donde nos fijamos en los datos de entrada y salida, evitando:

- Tener que cambiar los test del caso de uso después de cualquier cambio en la implementación del caso de uso que no implique cambio de lógica (refactor o mejora de código).
- Testear toda la lógica de la funcionalidad en su conjunto (capa aplicación y dominio), verificando que todas las partes juntas funcionan como se espera.

# Infrastructure

La capa infrastructure representa las implicaciones concretas en las integraciones de la aplicación, tanto de entrada (api rest, api grpc, consumidores eventos, etc) como de salida (base de datos, rest client, grpc client, publicadores de eventos, etc).

Dentro de la infraestructura se organizará el código mediante paquetes con el nombre de la tecnología con la que se integra, opcionalmente se puede añadir la versión:

- rest_api
- rest_api_v1
- grpc_api
- rest_client
- mongo
- postgres
- kafka

Dentro de la tecnologia, se dispondrá el código en varios paquetes:

| **Paquete Interno** | **Contenido** |
| --- | --- |
| `config` | (opcional) Clases de configuración |
| `dtos` | (opcional) DTO utilizado para la transferencia de información. Puede ser código autogenerado |
| `daos`  | (opcional) Objectos para trabajar con la base de datos |
| `services`  | (opcional) servicios necesarios en la integración, por ejemplo para la composición de datos recuperados en varias peticiones |
| `use_cases`  | (opcional) Casos de uso de administración de la integración, por ejemplo, limpieza de cache, migración de datos en la base de datos, etc |
| `mappers` | Clases encargadas de convertir DTO a objectos de dominio y viceversa |
| `<base>` | Adaptadores de entrada (controladores rest, consumidor eventos, etc) y salida (implementación de las clases del dominio) |

Por ejemplo:

```java
src/
	ecommerce/
		invoices/
      invoices/
        infrastructure/
	        rest_api/
		        dtos/
			        InvoiceRestDTO
			        InvoiceLineRestDTO
			      mappers/
				      InvoiceRestMapper
			      InvoiceRestController
			    mongo/
				    dtos/
					    InvoiceMongoDTO
					    InvoiceLineMongoDTO
					  daos/
						  InvoiceMongoDao
					  mappers/
						  InvoiceMongoMapper
						InvoiceMongoRepository
```

## Adaptadores Primarios

Los adaptadores primarios serán los responsables de la conversión de datos y realizar las llamadas a los casos de uso:

```java
@RestController
@RequiredArgsConstructor
public class InvoiceRestController implements InvoicesApi {

  private final InvoiceRestMapper invoiceRestMapper;

  private final UseCaseBus useCaseBus;

  @Override
  public ReviewListRestDTO searchReviews(
      Integer pageNumber,
      Integer pageSize,
      ReviewOrderRestDTO order,
      Boolean ascending,
      String entityId) {
    var params = invoiceRestMapper.toSearchInvoicesParams(pageNumber, pageSize, order, ascending);

    var invoices = useCaseBus.execute(params);

    return invoiceRestMapper.toSummaryListDTO(invoices);
  }
  
  // Other code...
}
```

## Adaptadores Secundarios

Mientras que los adaptadores secundarios son responsables de la conversión de datos y llamada a sistemas externos (base de datos, aplicaciones externas, gestores de colas de mensajes, etc)

```java
@Component
@RequiredArgsConstructor
public class InvoiceMongoRepository implements InvoicesRepository {

  private final InvoiceMongoMapper invoiceMongoMapper;

  private final InvoiceMongoDao invoiceMongoDao;

  @Override
  public Review save(Invoice invoice) {
    var invoiceDto = invoiceMongoMapper.toDTO(invoice);

    var resultDto = invoiceMongoDao.save(invoiceDto);

    return invoiceMongoMapper.toDomain(resultDto);
  }
  
  // Other code...
}

```

# Convenciones

## Código semántico

Todo el código generado debe ser lo más semántico posible para facilitar la verificación y mantenimiento. Para ello se recomiendo el uso de nombres significativos en:

- Variables: siempre intentar indicar que valor contiene mejor que usar nombres genéricos
- Métodos: utilizar siempre nombres que indique que acción realiza, por ejemplo:
    - getUser → findUserOrNull
    - validateUser → ensurePermissionsOfUser
    - createEntity → createEmptyInvoice
    - updateUser → paidInvoice

  En lógicas complejas, los métodos de entrada de los casos de uso o acciones de los agregados deberían ser un listado de los pasos a realizar. Llamando en cada paso a un método privado que lo implementa.

- Casos de Uso: mejor indicar que acción realiza el caso de uso (DeleteAllInvoiceOfUserUseCase) que indicar porque se realizar el caso de uso DeleteInvoiceOnUserDeleted). Ya que así el nombre indica el comportamiento del caso de uso.

<aside>
💡

Debido a que el código debería ser lo más explicativo/semántico posible, se desaconseja el uso de comentarios. También se descartan los comentarios estructurales de código.

Aun así puede existir casos en los que sería recomendable usar, por ejemplo:

- para aclarar los datos que se envían a apis externa y así aclarar mejor su comportamiento.
- notas aclarando cierta estrategia, query a base de datos, etc dejando claro porque se tiene que realizar así y se cambió otras manera. Así evitamos volver a introducir un posible bug en el sistema.
</aside>

## Paquete Shared

Todo código reutilizable entre varios agregados o bounded context, se debe mover a un paquete llamado shared. Este tipo de paquetes se pueden encontrar en 2 niveles de la aplicación:

- nivel dominio: código compartido entre varios bounded context, por tanto es candidato a mover a una librería común. Por ejemplo: clases de utilidad, validaciones, value object compartidos (email, address, etc), etc
- nivel módulo o bounded context: código compartido entre los agregados de un bounded contexto, por ejemplo: configuraciones, creación de beans compartidos (clientes rest), etc

```java
src/
	ecommerce/
		shared/
			domain/
				models/
					Email
					address/
						Address
						EsAddress
				services/
					ValueValidationUtils
		invoices/
			shared/
				domain/
					InvoiceConfiguration
				infrastructure/
					properties/
						InvoicePropertiesConfiguration
      invoices/
      clients/
```

## Métodos ensure

Los métodos con el prefijo `ensure` indican que además de realizar la validación acordada lanzarán una excepción si no se cumple:



```java
public static <T> void ensurePositive(int value, String attributeName) {
    if (value < 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is negative: " + value);
    }
    
    if (value == 0) {
      throw ValueValidationException.createOfMessage(attributeName + " is zero: " + value);
    }
  }
```

## Utilización var

Se ha acordado que para la definición de variables se utilizará la palabra reserva `var` y no el tipo, salvo que el compilador obligue a indicar el tipo.

Se llegó a este acuerdo para simplificar las líneas de código y motivar al desarrollador a indicar nombres representativos en las variables.