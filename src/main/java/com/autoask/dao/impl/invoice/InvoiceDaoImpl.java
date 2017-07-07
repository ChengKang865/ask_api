package com.autoask.dao.impl.invoice;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.invoice.InvoiceDao;
import com.autoask.entity.mongo.invoice.Invoice;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 16/11/19 19:48
 */
@Repository
public class InvoiceDaoImpl extends BaseDaoImpl<Invoice, String> implements InvoiceDao {
}
