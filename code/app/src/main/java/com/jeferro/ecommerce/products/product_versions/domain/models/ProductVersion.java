package com.jeferro.ecommerce.products.product_versions.domain.models;

import static com.jeferro.ecommerce.products.product_versions.domain.models.status.ProductStatus.PUBLISHED;
import static com.jeferro.ecommerce.products.product_versions.domain.models.status.ProductStatus.UNPUBLISHED;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.jeferro.ecommerce.support.parametrics.domain.models.values.ParametricValueId;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionCreated;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionDeleted;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionPublished;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUnpublished;
import com.jeferro.ecommerce.products.product_versions.domain.events.ProductVersionUpdated;
import com.jeferro.ecommerce.products.product_versions.domain.models.status.ProductStatus;
import com.jeferro.ecommerce.products.product_versions.domain.services.InstantTruncator;
import com.jeferro.shared.ddd.domain.models.aggregates.AggregateRoot;
import com.jeferro.shared.ddd.domain.models.aggregates.Metadata;
import com.jeferro.shared.ddd.domain.services.ValueValidator;
import com.jeferro.shared.locale.domain.models.LocalizedField;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductVersion extends AggregateRoot<ProductVersionId> {

  protected LocalizedField name;

  protected ProductStatus status;

  private final ParametricValueId typeId;

  private Instant endEffectiveDate;

  private BigDecimal price;

  private BigDecimal discount;

  private BigDecimal totalPrice;

  public ProductVersion(ProductVersionId id,
                        LocalizedField name,
                        ParametricValueId typeId,
                        Instant endEffectiveDate,
                        BigDecimal price,
                        BigDecimal discount,
                        BigDecimal totalPrice,
                        ProductStatus status,
                        long version,
                        Metadata metadata) {
    super(id, version, metadata);

    this.name = name;
    this.status = status;
    this.typeId = typeId;
    this.endEffectiveDate = endEffectiveDate;
    this.price = price;
    this.discount = discount;
    this.totalPrice = totalPrice;
  }

  public static ProductVersion create(
      ProductVersionId versionId,
      ParametricValueId typeId,
      LocalizedField name,
      BigDecimal price,
      BigDecimal discount,
      ProductVersion nextVersion) {
    ValueValidator.isNotNull(versionId, "versionId");
    ValueValidator.isNotNull(typeId, "typeId");
    ValueValidator.isNotNull(name, "name");
    ValueValidator.isZeroOrPositive(price, "price");
    ValueValidator.inRange(discount, 0, 100, "discount");

    if (nextVersion != null) {
      ValueValidator.ensure(
          () -> nextVersion.hasSameCode(versionId.getCode()),
          "Next product version hasn't belong new product version");
      ValueValidator.ensure(
          () -> nextVersion.isAfter(versionId.getEffectiveDate()),
          "Next product version is before than new product version");
    }

    var endEffectiveDate = nextVersion != null
        ? nextVersion.getEffectiveDate().minus(1, SECONDS)
        : null;

    var product =
        new ProductVersion(versionId,
            name,
            typeId,
            InstantTruncator.trunkToSeconds(endEffectiveDate),
            price,
            discount,
            calculateTotalPrice(price, discount),
            UNPUBLISHED,
            0L,
            null);

    var event = ProductVersionCreated.create(product);
    product.record(event);

    return product;
  }

  public void update(LocalizedField name, BigDecimal price, BigDecimal discount, long version) {
    ValueValidator.isNotNull(name, "name");
    ValueValidator.isZeroOrPositive(price, "price");
    ValueValidator.inRange(discount, 0, 100, "discount");

    ensureVersion(version);

    this.name = name;
    this.price = price;
    this.discount = discount;
    this.totalPrice = calculateTotalPrice(price, discount);

    var event = ProductVersionUpdated.create(this);
    record(event);
  }

  public void publish(long version) {
    ensureVersion(version);

    if (PUBLISHED.equals(status)) {
      return;
    }

    this.status = PUBLISHED;

    var event = ProductVersionPublished.create(this);
    record(event);
  }

  public void unpublish(long version) {
    ensureVersion(version);

    if (UNPUBLISHED.equals(status)) {
      return;
    }

    this.status = UNPUBLISHED;

    var event = ProductVersionUnpublished.create(this);
    record(event);
  }

  public void delete() {
    var event = ProductVersionDeleted.create(this);
    record(event);
  }

  public void expireBeforeVersion(ProductVersionId versionId) {
    endEffectiveDate = versionId.getEffectiveDate().minus(1, SECONDS);

    var event = ProductVersionUpdated.create(this);
    record(event);
  }

  public void notExpire() {
    endEffectiveDate = null;

    var event = ProductVersionUpdated.create(this);
    record(event);
  }

  private static BigDecimal calculateTotalPrice(BigDecimal price, BigDecimal discount) {
    var totalDiscount = price.multiply(discount);

    return price.subtract(totalDiscount);
  }

  public boolean isPublished() {
    return PUBLISHED.equals(status);
  }

  public boolean isUnpublished() {
    return UNPUBLISHED.equals(status);
  }

  public ProductCode getCode() {
    return id.getCode();
  }

  public boolean hasSameCode(ProductCode code) {
    return id.getCode().equals(code);
  }

  private Boolean isAfter(Instant effectiveDate) {
    return id.getEffectiveDate().isAfter(effectiveDate);
  }

  public Instant getEffectiveDate() {
    return id.getEffectiveDate();
  }
}
