let now = new Date()

db['products'].insertMany([
    {
        _id: "0000001::2025-01-01T09:00:00.00Z",
        code: "0000001",
        effectiveDate: ISODate("2025-01-01T09:00:00.00Z"),
        endEffectiveDate: ISODate("2025-01-15T08:59:59.00Z"),
        typeId: "fruit",
        name: {
          "en-US": "Apple",
          "es-ES": "Manzana",
        },
        status: "UNPUBLISHED",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "0000001::2025-01-15T09:00:00.00Z",
        code: "0000001",
        effectiveDate: ISODate("2025-01-15T09:00:00.00Z"),
        endEffectiveDate: null,
        typeId: "fruit",
        name: {
          "en-US": "Apple",
          "es-ES": "Manzana",
        },
        status: "PUBLISHED",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "0000002::2025-01-01T09:00:00.00Z",
        code: "0000002",
        effectiveDate: ISODate("2025-01-01T09:00:00.00Z"),
        endEffectiveDate: null,
        typeId: "fruit",
        name: {
          "en-US": "Kiwi",
          "es-ES": "Kiwi",
        },
        status: "PUBLISHED",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "0000003::2025-01-01T09:00:00.00Z",
        code: "0000003",
        effectiveDate: ISODate("2025-01-01T09:00:00.00Z"),
        endEffectiveDate: null,
        typeId: "fruit",
        name: {
          "en-US": "Banana",
          "es-ES": "Banana",
        },
        status: "UNPUBLISHED",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    }
])
