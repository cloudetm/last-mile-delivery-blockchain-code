'use strict';
/**
 * Write your transction processor functions here
 */

/**
 * Sample transaction
 * @param {delivery.SendToTransporter} tx
 * @transaction
 */
function SendtoTransporter(tx) {
    var product = tx.product
    var transporter = tx.transporter
    
    product.productHolder = transporter
    product.productStatus = "sent to transporter " + transporter.holderId
    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
})
}
/**
 * Sample transaction
 * @param {delivery.SendToWarehouse} tx
 * @transaction
 */
function SendtoWarehouse(tx) {
    var product = tx.product
    var warehouse = tx.warehouse

    product.productHolder = warehouse
    product.productStatus = "sent to warehouse " + warehouse.holderId
    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
})
}

/**
 * Sample transaction
 * @param {delivery.SendToCourier} tx
 * @transaction
 */
function SendtoCourier(tx) {
    var product = tx.product
    var courier = tx.courier

    product.productHolder = courier
    product.productStatus = "sent to Courier " + courier.holderId
    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
}

/**
 * Sample transaction
 * @param {delivery.SendToCustomer} tx
 * @transaction
 */
function SendtoCustomer(tx) {
    var product = tx.product
    var courier = tx.customer

    product.productHolder = customer
    product.productStatus = "Delivered to Customer " + customer.holderId
    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
}

/**
 * Sample transaction
 * @param {delivery.RecieveProduct} tx
 * @transaction
 */
function RecieveProduct (tx){
    var product = tx.product
    product.productStatus = "Recieved by " + product.productHolder.holderId

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
    
}
