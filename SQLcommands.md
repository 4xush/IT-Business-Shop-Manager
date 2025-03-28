## Access db -
cd /path/to/project
Desktop/ShopSync Manager

## To Start the SQLite shell:
sqlite3 shopsync.db

## View the tables:
.tables

## View table structure:
.schema Products

Query data:
SELECT * FROM Products;
SELECT * FROM Sales;

.schema Repairs
PRAGMA table_info(Repairs);

## Remove db
remove db : -rm /home/ayush/Desktop/ShopSync Manager/shopsync.db

## drop particular schema
DROP TABLE IF EXISTS sales;

## Rename specific table
ALTER TABLE Products RENAME TO products;
ALTER TABLE Repairs RENAME TO repairs;
ALTER TABLE Sales RENAME TO sales;
ALTER TABLE Recharges RENAME TO recharges;
