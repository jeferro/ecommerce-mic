let now = new Date()

db['product_versions'].insertMany([
    {
        _id: "apple::2025-01-01T09:00:00.00Z",
        code: "apple",
        effectiveDate: ISODate("2025-01-01T09:00:00.00Z"),
        endEffectiveDate: ISODate("2025-01-15T08:59:59.00Z"),
        typeId: "fruit",
        name: {
          "en-US": "Apple",
          "es-ES": "Manzana",
        },
        "price": "1.25",
        "discount": "0",
        "totalPrice": "1.25",
        status: "UNPUBLISHED",
        version: 1,
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "apple::2025-01-15T09:00:00.00Z",
        code: "apple",
        effectiveDate: ISODate("2025-01-15T09:00:00.00Z"),
        endEffectiveDate: null,
        typeId: "fruit",
        name: {
          "en-US": "Apple",
          "es-ES": "Manzana",
        },
        "price": "1.25",
        "discount": "0",
        "totalPrice": "1.25",
        status: "PUBLISHED",
        version: 1,
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "kiwi::2025-01-01T09:00:00.00Z",
        code: "kiwi",
        effectiveDate: ISODate("2025-01-01T09:00:00.00Z"),
        endEffectiveDate: null,
        typeId: "fruit",
        name: {
          "en-US": "Kiwi",
          "es-ES": "Kiwi",
        },
        "price": "3.5",
        "discount": "0",
        "totalPrice": "3.5",
        status: "PUBLISHED",
        version: 1,
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "banana::2025-01-01T09:00:00.00Z",
        code: "banana",
        effectiveDate: ISODate("2025-01-01T09:00:00.00Z"),
        endEffectiveDate: null,
        typeId: "fruit",
        name: {
          "en-US": "Banana",
          "es-ES": "Banana",
        },
        "price": "3",
        "discount": "0",
        "totalPrice": "3",
        status: "UNPUBLISHED",
        version: 1,
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    }
])
