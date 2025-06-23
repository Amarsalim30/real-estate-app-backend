N:B//This is just a visualization for the relationships between the entities in the system.
The data should not be taken as real-amarsalimprojects .

[User] 1───1 [BuyerProfile] 1───* [Unit] 1───1 [Invoice] 1───* [Payment] 1───* [PaymentDetail]
                                     │                             │                │
                                     └── belongs to [Project]      └── belongs to   └── has payment method & metadata
                                                                            [invoice] 
[User]
- id
- username
- email
- password
- role (enum)

[BuyerProfile]
- id
- email
- phone
- address, city, etc.
- nationalId,kraPin
- user_id (FK → User.id)

[Project]
- id
- name
- status (enum)
- ...(more fields)

[Unit]
- id
- name
- status (enum)
- type (enum)
- price
- project_id (FK → Project)
- buyer_id (FK → BuyerProfile)

[Invoice]
- id
- status (enum)
- totalAmount
- issuedDate
- unit_id (FK → Unit)
- buyer_id (FK → BuyerProfile)

[Payment]
- id
- amount
- status (enum)
- method (enum)
- invoice_id (FK → Invoice)
- buyer_id (FK → BuyerProfile)

[PaymentDetail]
- id
- amount
- status (enum)
- method (enum)
- transactionId
- buyer_id (FK → BuyerProfile)
- invoice_id (FK → Invoice)
- payment_id (FK → Payment)
- bankDetails (embedded)
- cardDetails (embedded)
- checkDetails (embedded)
