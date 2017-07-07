package com.autoask.dao.invoice;

import com.autoask.dao.BaseDao;
import com.autoask.entity.mongo.invoice.Invoice;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 16/11/19 19:48
 */
@Repository
public interface InvoiceDao extends BaseDao<Invoice, String> {
}
