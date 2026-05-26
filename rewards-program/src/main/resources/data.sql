INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 120.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 75.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 200.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 45.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (1, 99.00, CURRENT_DATE);

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (2, 150.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (2, 60.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (2, 310.00, CURRENT_DATE);

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (3, 100.00, DATEADD('MONTH', -2, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (3, 50.00, DATEADD('MONTH', -1, CURRENT_DATE));

INSERT INTO transactions (customer_id, amount, transaction_date)
VALUES (3, 500.00, CURRENT_DATE);