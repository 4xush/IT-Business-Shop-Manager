
## access db -
cd /path/to/your/project
Desktop/IT-Business-Shop

Start the SQLite shell:
sqlite3 kingcom.db

View the tables:
.tables

View table structure:
.schema Products

Query data:
SELECT * FROM Products;
SELECT * FROM Sales;


.schema Repairs
PRAGMA table_info(Repairs);

## remove db
remove db -rm /home/ayush/Desktop/IT-Business-Shop/kingcom.db


## drop particular schema
DROP TABLE IF EXISTS sales;

ALTER TABLE Products RENAME TO products;
ALTER TABLE Repairs RENAME TO repairs;
ALTER TABLE Sales RENAME TO sales;
ALTER TABLE Recharges RENAME TO recharges;
