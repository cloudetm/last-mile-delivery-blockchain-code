'use strict';
/**
 * Write your transction processor functions here
 */

/**
 * Sample transaction
 * @param {delivery.CreateProuduct} tx
 * @transaction
 */
function CreateProduct(tx){
    var productId = tx.productId
    var holder = tx.holder

    var factory1 = getFactory()

    var timeStatus = factory1.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Product reached to " + holder.holderId

    var factory2 = getFactory()

    var product = factory2.newResource('delivery','Product',productId)
    product.productStatus = "Product reached to " + holder.holderId
    product.productHolder = factory2.newRelationship('delivery','ProductHolder',holder.$identifier)

    var factory3 = getFactory()
    var productHistory = factory3.newResource('delivery','ProductHistory',productId)
    productHistory.productHistory = []
  	productHistory.productHistory.push(timeStatus)

    return getAssetRegistry('delivery.Product')
    .then(function(assetRegistry){
        return assetRegistry.add(product)
    })
    .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.add(productHistory)
        }))    
}

/**
 * Sample transaction
 * @param {delivery.SendToTransporter} tx
 * @transaction
 */
function SendtoTransporter(tx) {
    var product = tx.product
    var productHistory = tx.productHistory
    var transporter = tx.transporter

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "sent to transporter " + transporter.holderId
    
    product.productHolder = transporter
    product.productStatus = "sent to transporter " + transporter.holderId

    productHistory.productHistory.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        })) 
}
/**
 * Sample transaction
 * @param {delivery.SendToWarehouse} tx
 * @transaction
 */
function SendtoWarehouse(tx) {
    var product = tx.product
    var warehouse = tx.warehouse
    var productHistory = tx.productHistory
    
    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "sent to warehouse " + warehouse.holderId
    
    product.productHolder = warehouse
    product.productStatus = "sent to warehouse " + warehouse.holderId

    productHistory.productHistory.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        })) 
}

/**
 * Sample transaction
 * @param {delivery.SendToCourier} tx
 * @transaction
 */
function SendtoCourier(tx) {
    var product = tx.product
    var courier = tx.courier
    var productHistory = tx.productHistory

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "sent to courier " + courier.holderId

    product.productHolder = courier
    product.productStatus = "sent to Courier " + courier.holderId

    productHistory.productHistory.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        })) 
}

/**
 * Sample transaction
 * @param {delivery.SendToCustomer} tx
 * @transaction
 */
function SendtoCustomer(tx) {
    var product = tx.product
    var customer = tx.customer
    var productHistory = tx.productHistory

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Delivered to Customer " + customer.holderId

    product.productHolder = customer
    product.productStatus = "Delivered to Customer " + customer.holderId

    productHistory.productHistory.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        })) 
}

/**
 * Sample transaction
 * @param {delivery.RecieveProduct} tx
 * @transaction
 */
function RecieveProducts (tx){
    var product = tx.product
    var productHistory = tx.productHistory

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Recieved by " + product.productHolder.holderId
    
    product.productStatus = "Recieved by " + product.productHolder.holderId

    productHistory.productHistory.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        })) 
    
}
