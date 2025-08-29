let now = new Date()

db['reviews'].insertMany([
    {
        _id: "user::products::apple",
        username: "user",
        entityId: {
            domain: "products",
            id: "apple"
        },
        locale: "en-US",
        comment: "Apple is so good",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    },
    {
        _id: "admin::products::apple",
        username: "admin",
        entityId: {
            domain: "products",
            id: "apple"
        },
        locale: "en-US",
        comment: "I prefer bananas",
        metadata: {
            createdBy: 'admin',
            createdAt: now,
            updatedBy: 'admin',
            updatedAt: now
        }
    },
    {
        _id: "user::products::kiwi",
        username: "user",
        entityId: {
            domain: "products",
            id: "kiwi"
        },
        locale: "en-US",
        comment: "Love kiwis",
        metadata: {
            createdBy: 'user',
            createdAt: now,
            updatedBy: 'user',
            updatedAt: now
        }
    }
])
