## 🧪 Complete Testing Examples

### 1. Create an Account

```bash
curl -X POST https://finance-tracker-backend-segu.onrender.com/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "secure123"
  }'
TOKEN="your-token-here"

curl -X POST https://finance-tracker-backend-segu.onrender.com/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "description": "Monthly Salary",
    "amount": 5000.00,
    "category": "Salary",
    "type": "INCOME",
    "date": "2024-03-01"
  }'
# Rent
curl -X POST https://finance-tracker-backend-segu.onrender.com/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "description": "Monthly Rent",
    "amount": 1500.00,
    "category": "Bills",
    "type": "EXPENSE",
    "date": "2024-03-01"
  }'

# Groceries
curl -X POST https://finance-tracker-backend-segu.onrender.com/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "description": "Groceries",
    "amount": 250.00,
    "category": "Food",
    "type": "EXPENSE",
    "date": "2024-03-05"
  }'
curl -H "Authorization: Bearer $TOKEN" \
  https://finance-tracker-backend-segu.onrender.com/api/transactions
curl -H "Authorization: Bearer $TOKEN" \
  "https://finance-tracker-backend-segu.onrender.com/api/transactions/summary?year=2024&month=3"
//Response
{
  "totalIncome": 5000.00,
  "totalExpenses": 1750.00,
  "balance": 3250.00,
  "expensesByCategory": {
    "Bills": 1500.00,
    "Food": 250.00
  },
  "transactionCount": 2
}

curl -X PUT https://finance-tracker-backend-segu.onrender.com/api/transactions/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "description": "Monthly Salary + Bonus",
    "amount": 5500.00,
    "category": "Salary",
    "type": "INCOME",
    "date": "2024-03-01"
  }'

curl -X DELETE https://finance-tracker-backend-segu.onrender.com/api/transactions/2 \
  -H "Authorization: Bearer $TOKEN"

Income Transactions:


[
  {
    "description": "March Salary",
    "amount": 5000.00,
    "category": "Salary",
    "type": "INCOME",
    "date": "2024-03-01"
  },
  {
    "description": "Freelance Project",
    "amount": 800.00,
    "category": "Freelance",
    "type": "INCOME",
    "date": "2024-03-15"
  }
]
Expense Transactions:


[
  {
    "description": "Rent",
    "amount": 1500.00,
    "category": "Bills",
    "type": "EXPENSE",
    "date": "2024-03-01"
  },
  {
    "description": "Groceries",
    "amount": 320.50,
    "category": "Food",
    "type": "EXPENSE",
    "date": "2024-03-05"
  },
  {
    "description": "Netflix + Spotify",
    "amount": 30.00,
    "category": "Entertainment",
    "type": "EXPENSE",
    "date": "2024-03-10"
  },
  {
    "description": "Gas",
    "amount": 120.00,
    "category": "Transport",
    "type": "EXPENSE",
    "date": "2024-03-12"
  }
]
