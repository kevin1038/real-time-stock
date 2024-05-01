INSERT INTO security (ticker, type, expected_return, annualized_sd, init_price) VALUES ('AAPL', 'STOCK', 0.05, 0.2, 110);
INSERT INTO security (ticker, type, expected_return, annualized_sd, init_price) VALUES ('TELSA', 'STOCK', 0.05, 0.2, 450);
INSERT INTO security (ticker, type, strike, maturity_date, underlying_stock) VALUES ('AAPL-OCT-2024-110-C', 'CALL', 110, '2024-10-25', 'AAPL');
INSERT INTO security (ticker, type, strike, maturity_date, underlying_stock) VALUES ('AAPL-OCT-2024-110-P', 'PUT', 110, '2024-10-25', 'AAPL');
INSERT INTO security (ticker, type, strike, maturity_date, underlying_stock) VALUES ('TELSA-NOV-2024-400-C', 'CALL', 400, '2024-11-29', 'TELSA');
INSERT INTO security (ticker, type, strike, maturity_date, underlying_stock) VALUES ('TELSA-DEC-2024-400-P', 'PUT', 400, '2024-12-27', 'TELSA');
