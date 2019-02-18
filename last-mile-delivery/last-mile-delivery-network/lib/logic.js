'use strict';
/**
 * Write your transction processor functions here
 */

/**
 * Sample transaction
 * @param {delivery.CreateWarehouse} tx
 * @transaction
 */
function CreateWarehouse(tx){
    var holderId = tx.holderId
    var location = tx.location

    var factory1 = getFactory()

    var timeStatus = factory1.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Warehouse with Id " + holderId + " created"

    var factory2 = getFactory()

    var warehouse = factory2.newResource('delivery','Warehouse',holderId)
    warehouse.location = location

    var factory3 = getFactory()
    var warehouseHistory = factory3.newResource('delivery','WarehouseHistory',holderId)
    warehouseHistory.history = []
  	warehouseHistory.history.push(timeStatus)

    return getParticipantRegistry('delivery.Warehouse')
    .then(function(participantRegistry){
        return participantRegistry.add(warehouse)
    })
    .then(getAssetRegistry('delivery.WarehouseHistory')
        .then(function(assetRegistry){
            return assetRegistry.add(warehouseHistory)
        }))    
}

/**
 * Sample transaction
 * @param {delivery.CreateTransporter} tx
 * @transaction
 */
function CreateTransporter(tx){
    var holderId = tx.holderId

    var factory1 = getFactory()

    var timeStatus = factory1.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Transporter with Id " + holderId + " created"

    var factory2 = getFactory()

    var transporter = factory2.newResource('delivery','Transporter',holderId)

    var factory3 = getFactory()
    var transporterHistory = factory3.newResource('delivery','TransporterHistory',holderId)
    transporterHistory.history = []
  	transporterHistory.history.push(timeStatus)

    return getParticipantRegistry('delivery.Transporter')
    .then(function(participantRegistry){
        return participantRegistry.add(transporter)
    })
    .then(getAssetRegistry('delivery.TransporterHistory')
        .then(function(assetRegistry){
            return assetRegistry.add(transporterHistory)
        }))    
}

/**
 * Sample transaction
 * @param {delivery.CreateCourier} tx
 * @transaction
 */
function CreateCourier(tx){
    var holderId = tx.holderId

    var factory1 = getFactory()

    var timeStatus = factory1.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Courier with Id " + holderId + " created"

    var factory2 = getFactory()

    var courier = factory2.newResource('delivery','Courier',holderId)

    var factory3 = getFactory()
    var courierHistory = factory3.newResource('delivery','CourierHistory',holderId)
    courierHistory.history = []
    courierHistory.history.push(timeStatus)

    return getParticipantRegistry('delivery.Courier')
    .then(function(participantRegistry){
        return participantRegistry.add(courier)
    })
    .then(getAssetRegistry('delivery.CourierHistory')
        .then(function(assetRegistry){
            return assetRegistry.add(courierHistory)
        }))    
}

/**
 * Sample transaction
 * @param {delivery.CreateCustomer} tx
 * @transaction
 */
function CreateCustomer(tx){
    var holderId = tx.holderId

    var factory1 = getFactory()

    var timeStatus = factory1.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Customer with Id " + holderId + " created"

    var factory2 = getFactory()

    var customer = factory2.newResource('delivery','Customer',holderId)

    var factory3 = getFactory()
    var customerHistory = factory3.newResource('delivery','CustomerHistory',holderId)
    customerHistory.history = []
    customerHistory.history.push(timeStatus)

    return getParticipantRegistry('delivery.Customer')
    .then(function(participantRegistry){
        return participantRegistry.add(customer)
    })
    .then(getAssetRegistry('delivery.CustomerHistory')
        .then(function(assetRegistry){
            return assetRegistry.add(customerHistory)
        }))
}


/**
 * Sample transaction
 * @param {delivery.CreateProduct} tx
 * @transaction
 */
function CreateProduct(tx){
    var productId = tx.productId
    var latitude = tx.latitude
    var longitude = tx.longitude
    var holder = tx.holder
    var holderHistory = tx.holderHistory
    var fqAssetName = "delivery." + tx.holderHistory.$type

    var factory0 = getFactory()
    var timeStatus = factory0.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = "Product " + productId +" reached to " + holder.holderId

    var factory1 = getFactory()
    var timeStatusLocation = factory1.newConcept('delivery','TimeStatusLocation')
    timeStatusLocation.time = Date(0)
    timeStatusLocation.status = "Product " + productId +" reached to " + holder.holderId
    timeStatusLocation.latitude = latitude
    timeStatusLocation.longitude = longitude

    var factory2 = getFactory()
    var product = factory2.newResource('delivery','Product',productId)
    product.productStatus = "Product " + productId +" reached to " + holder.holderId
    product.productHolder = factory2.newRelationship('delivery','ProductHolder',holder.$identifier)
    product.latitude = latitude
    product.longitude = longitude

    var factory3 = getFactory()
    var productHistory = factory3.newResource('delivery','ProductHistory',productId)
    productHistory.history = []
    productHistory.history.push(timeStatusLocation)
    
    holderHistory.history.push(timeStatus)

    // return getAssetRegistry('delivery.Product')
    // .then(function(assetRegistry){
    //     return assetRegistry.add(product)
    // })
    // .then(getAssetRegistry('delivery.ProductHistory')
    //     .then(function(assetRegistry){
    //         return assetRegistry.add(productHistory)
    //     }))
    // .then(getAssetRegistry(fqAssetName)
    //     .then(function(assetRegistry){
    //         return assetRegistry.update(holderHistory)
    //     })
    // )
    getAssetRegistry('delivery.Product')
      .then(function(assetRegistry){
          return assetRegistry.add(product)
      })
    getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.add(productHistory)
        })
    getAssetRegistry(fqAssetName)
        .then(function(assetRegistry){
            return assetRegistry.update(holderHistory)
        })
}

/**
 * Sample transaction
 * @param {delivery.SendToTransporter} tx
 * @transaction
 */
function SendtoTransporter(tx) {
    var product = tx.product
    var latitude = tx.latitude
    var longitude = tx.longitude
    var productHistory = tx.productHistory
    var transporter = tx.transporter
    var currentHolderHistory = tx.currentHolderHistory
    var fqAssetName = "delivery." + tx.currentHolderHistory.$type

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = product.productId + " sent to transporter " + transporter.holderId

    var factory1 = getFactory()
    var timeStatusLocation = factory1.newConcept('delivery','TimeStatusLocation')
    timeStatusLocation.time = Date(0)
    timeStatusLocation.status = product.productId + " sent to transporter " + transporter.holderId
    timeStatusLocation.latitude = latitude
    timeStatusLocation.longitude = longitude
    
    product.productHolder = transporter
    product.productStatus = "sent to transporter " + transporter.holderId
    product.latitude = latitude
    product.longitude = longitude

    productHistory.history.push(timeStatusLocation)

    currentHolderHistory.history.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        }))
        .then(getAssetRegistry(fqAssetName)
        .then(function(assetRegistry){
            return assetRegistry.update(currentHolderHistory)
        })) 
    }
/**
 * Sample transaction
 * @param {delivery.SendToWarehouse} tx
 * @transaction
 */
function SendtoWarehouse(tx) {
    var product = tx.product
    var latitude = tx.latitude
    var longitude = tx.longitude
    var warehouse = tx.warehouse
    var productHistory = tx.productHistory
    var currentHolderHistory = tx.currentHolderHistory
    var fqAssetName = "delivery." + tx.currentHolderHistory.$type

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = product.productId + " sent to warehouse " + warehouse.holderId

    var factory1 = getFactory()
    var timeStatusLocation = factory1.newConcept('delivery','TimeStatusLocation')
    timeStatusLocation.time = Date(0)
    timeStatusLocation.status = product.productId + " sent to warehouse " + warehouse.holderId
    timeStatusLocation.latitude = latitude
    timeStatusLocation.longitude = longitude
    
    product.productHolder = warehouse
    product.productStatus = "sent to warehouse " + warehouse.holderId
    product.latitude = latitude
    product.longitude = longitude

    productHistory.history.push(timeStatusLocation)

    currentHolderHistory.history.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        }))
        .then(getAssetRegistry(fqAssetName)
        .then(function(assetRegistry){
            return assetRegistry.update(currentHolderHistory)
        })) 
    }

/**
 * Sample transaction
 * @param {delivery.SendToCourier} tx
 * @transaction
 */
function SendtoCourier(tx) {
    var product = tx.product
    var latitude = tx.latitude
    var longitude = tx.longitude
    var courier = tx.courier
    var productHistory = tx.productHistory
    var currentHolderHistory = tx.currentHolderHistory
    var fqAssetName = "delivery." + tx.currentHolderHistory.$type

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = product.productId + " sent to courier " + courier.holderId

    var factory1 = getFactory()
    var timeStatusLocation = factory1.newConcept('delivery','TimeStatusLocation')
    timeStatusLocation.time = Date(0)
    timeStatusLocation.status = product.productId + " sent to courier " + courier.holderId
    timeStatusLocation.latitude = latitude
    timeStatusLocation.longitude = longitude
    
    product.productHolder = courier
    product.productStatus = "sent to courier " + courier.holderId
    product.latitude = latitude
    product.longitude = longitude

    productHistory.history.push(timeStatusLocation)

    currentHolderHistory.history.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        }))
        .then(getAssetRegistry(fqAssetName)
        .then(function(assetRegistry){
            return assetRegistry.update(currentHolderHistory)
        })) 
    }

/**
 * Sample transaction
 * @param {delivery.SendToCustomer} tx
 * @transaction
 */
function SendtoCustomer(tx) {
    var product = tx.product
    var latitude = tx.latitude
    var longitude = tx.longitude
    var customer = tx.customer
    var productHistory = tx.productHistory
    var currentHolderHistory = tx.currentHolderHistory
    var fqAssetName = "delivery." + tx.currentHolderHistory.$type

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = product.productId + " sent to customer " + customer.holderId

    var factory1 = getFactory()
    var timeStatusLocation = factory1.newConcept('delivery','TimeStatusLocation')
    timeStatusLocation.time = Date(0)
    timeStatusLocation.status = product.productId + " sent to customer " + customer.holderId
    timeStatusLocation.latitude = latitude
    timeStatusLocation.longitude = longitude
    
    product.productHolder = customer
    product.productStatus = "sent to customer " + customer.holderId
    product.latitude = latitude
    product.longitude = longitude

    productHistory.history.push(timeStatusLocation)

    currentHolderHistory.history.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        }))
        .then(getAssetRegistry(fqAssetName)
        .then(function(assetRegistry){
            return assetRegistry.update(currentHolderHistory)
        })) 
    }

/**
 * Sample transaction
 * @param {delivery.RecieveProduct} tx
 * @transaction
 */
function RecieveProducts (tx){
    var product = tx.product
    var receiver = tx.receiver
    var productHistory = tx.productHistory
    var recieverHistory = tx.recieverHistory
    var fqAssetName = "delivery." + tx.recieverHistory.$type

    if (product.productHolder != receiver){
        throw new Error('Product is not with the this holder')
    }

    var factory = getFactory()
    var timeStatus = factory.newConcept('delivery','TimeStatus')
    timeStatus.time = Date(0)
    timeStatus.status = product.productId + " received by " + product.productHolder.holderId

    var factory1 = getFactory()
    var timeStatusLocation = factory1.newConcept('delivery','TimeStatusLocation')
    timeStatusLocation.time = Date(0)
    timeStatusLocation.status = product.productId + " received by " + product.productHolder.holderId
    timeStatusLocation.latitude = product.latitude
    timeStatusLocation.longitude = product.longitude

    product.productStatus = "Recieved by " + product.productHolder.holderId

    productHistory.history.push(timeStatusLocation)

    recieverHistory.history.push(timeStatus)

    return getAssetRegistry('delivery.Product')
        .then(function(assetRegistry){
            return assetRegistry.update(product)
        })
        .then(getAssetRegistry('delivery.ProductHistory')
        .then(function(assetRegistry){
            return assetRegistry.update(productHistory)
        }))
        .then(getAssetRegistry(fqAssetName)
        .then(function(assetRegistry){
            return assetRegistry.update(recieverHistory)
        }))
    
    }
