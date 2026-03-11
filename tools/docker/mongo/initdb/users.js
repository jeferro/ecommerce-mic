let now = new Date()

db['users'].insertMany([
    {
        _id: "user",
        encodedPassword: "$2a$10$u0DOZw4GBDChvSivqDHKeuq7IEGtnKuT69F0/wCkeM3ytatm2AR1G",
        roles: [
            'user'
        ],
        version: 1,
        metadata: {
            createdAt: now,
            createdBy: 'user',
            updatedAt: now,
            updatedBy: 'user'
        }
    },
    {
        _id: "admin",
        encodedPassword: "$2a$10$IQCGQrW26LPiKk8vMTui9edy4Zy4jHgN6kwga5aGM1Ri27v4kqE1u",
        roles: [
            'user',
            'admin'
        ],
        version: 1,
        metadata: {
            createdAt: now,
            createdBy: 'user',
            updatedAt: now,
            updatedBy: 'user'
        }
    }
])
