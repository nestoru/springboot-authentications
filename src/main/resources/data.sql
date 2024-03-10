INSERT INTO users (username, password, enabled) VALUES 
('admin', '$2a$10$caM6aZ2N0KP.QXAByTRxPuKIB49uRXlPZRLDR.mFDYLq8KnSOA5PW', true), 
('operator', '$2a$10$1oPvchruN3q3hirtk4ecMOUifOiytnvrlldCfDbCLE3ftMOedWMjy', true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO authorities (username, authority) VALUES 
('admin', 'ROLE_ADMIN'), 
('operator', 'ROLE_OPERATOR') 
ON CONFLICT (username, authority) DO NOTHING;

