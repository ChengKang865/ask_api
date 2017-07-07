package com.autoask.service.invoice;

import com.autoask.common.exception.ApiException;
import com.autoask.entity.mongo.invoice.Invoice;

/**
 * @author hyy
 * @create 16/11/19 19:50
 */
public interface InvoiceService {

    Invoice getUserInvoice() throws ApiException;

    Invoice updateOrInsertInvoice(Invoice invoice) throws ApiException;
    
    Invoice getById(String id) throws ApiException;
}
