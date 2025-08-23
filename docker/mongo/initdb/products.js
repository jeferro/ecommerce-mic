let now = new Date()

db['products'].insertMany([
    {
        _id: "0000001::2025-01-01T09:00:00.00Z",
        typeId: "fruit",
        name: {
          "en-US": "Apple",
          "es-ES": "Manzana",
        },
        effectiveDate: "2025-01-01T09:00:00.00Z",
        endEffectiveDate: "2025-01-15T08:59:59.00Z",
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
        typeId: "fruit",
        name: {
          "en-US": "Apple",
          "es-ES": "Manzana",
        },
        effectiveDate: "2025-01-15T09:00:00.00Z",
        endEffectiveDate: null,
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
        typeId: "fruit",
        name: {
          "en-US": "Kiwi",
          "es-ES": "Kiwi",
        },
        effectiveDate: "2025-01-01T09:00:00.00Z",
        endEffectiveDate: null,
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
        typeId: "fruit",
        name: {
          "en-US": "Banana",
          "es-ES": "Banana",
        },
        effectiveDate: "2025-01-01T09:00:00.00Z",
        endEffectiveDate: null,
        status: "UNPUBLISHED",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    }
])
