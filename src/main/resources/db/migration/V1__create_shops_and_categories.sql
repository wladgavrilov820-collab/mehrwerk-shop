CREATE TABLE categories (
                            id VARCHAR(64) NOT NULL PRIMARY KEY,
                            name NVARCHAR(255) NOT NULL
);

CREATE TABLE shops (
                       id VARCHAR(64) NOT NULL PRIMARY KEY,
                       name NVARCHAR(255) NOT NULL,
                       description NVARCHAR(MAX) NULL
);

CREATE TABLE shop_categories (
                                 shop_id VARCHAR(64) NOT NULL,
                                 category_id VARCHAR(64) NOT NULL,

                                 CONSTRAINT pk_shop_categories
                                     PRIMARY KEY (shop_id, category_id),

                                 CONSTRAINT fk_shop_categories_shop
                                     FOREIGN KEY (shop_id) REFERENCES shops(id),

                                 CONSTRAINT fk_shop_categories_category
                                     FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_shop_categories_category_id
    ON shop_categories(category_id);