-- customer table
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Sam','(925)-123-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Sergey','(925)-223-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Bob', ,'(925)-323-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Robert','(925)-423-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Alex', '(925)-523-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Savva','(925)-623-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Josh', '(925)-723-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Patrick','(925)-823-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Andy', '(925)-923-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Eric', '(925)-013-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Chris','(925)-023-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Raj', '(925)-033-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Vic', '(925)-043-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Rich','(925)-053-4567', NOW());
INSERT INTO customer(id, name, phone, tstamp) values( nextval( 'hibernate_sequence') , 'Mark','(925)-063-4567', NOW());

-- barber table 

INSERT INTO barber(id, name, haircutprice, tstamp) values( nextval( 'hibernate_sequence') , 'Jovanni', 15.20, NOW());
INSERT INTO barber(id, name, haircutprice, tstamp) values( nextval( 'hibernate_sequence') , 'Marchello', 24.99, NOW());

-- visit table

INSERT INTO visit(id, customerName, customerPhone, barberName,  startTimeVisit, endTimeVisit, hairCutPrice) values( nextval( 'hibernate_sequence') , 'Sam','(925)-123-4567', 'Jovanni', NOW(), NOW(), 15.20);
INSERT INTO visit(id, customerName, customerPhone, barberName,  startTimeVisit, endTimeVisit, hairCutPrice) values( nextval( 'hibernate_sequence') , 'Sergey','(925)-223-4567', 'Marchello', NOW(), NOW(), 24.99);
INSERT INTO visit(id, customerName, customerPhone, barberName,  startTimeVisit, endTimeVisit, hairCutPrice) values( nextval( 'hibernate_sequence') , 'Bob','(925)-323-4567', 'Marchello', NOW(), NOW(), 24.99);
