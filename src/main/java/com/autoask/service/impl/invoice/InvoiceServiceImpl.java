package com.autoask.service.impl.invoice;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.common.util.LoginUtil;
import com.autoask.dao.invoice.InvoiceDao;
import com.autoask.entity.mongo.invoice.CommonInvoice;
import com.autoask.entity.mongo.invoice.Invoice;
import com.autoask.entity.mongo.invoice.VatInvoice;
import com.autoask.service.invoice.InvoiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author hyy
 * @create 16/11/19 19:50
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;

    @Override
    public Invoice getUserInvoice() throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();
        return invoiceDao.findOne(Query.query(Criteria.where("userId").is(userId)));
    }

    @Override
    public Invoice updateOrInsertInvoice(Invoice invoice) throws ApiException {
        String userId = LoginUtil.getSessionInfo().getLoginId();

        if (null == invoice) {
            throw new ApiException("数据非法");
        }
        if (!Constants.InvoiceType.TYPE_LIST.contains(invoice.getType())) {
            throw new ApiException("类型参数非法");
        }
        if (StringUtils.equals(Constants.InvoiceType.COMMON, invoice.getType())) {
            CommonInvoice commonInvoice = invoice.getCommonInvoice();
            if (null == commonInvoice) {
                throw new ApiException("参数不能为空");
            }
            if (StringUtils.isEmpty(commonInvoice.getHeader())) {
                throw new ApiException("抬头不能为空");
            }
        } else {
            VatInvoice vatInvoice = invoice.getVatInvoice();
            if (null == vatInvoice) {
                throw new ApiException("参数不能为空");
            }
            if (StringUtils.isEmpty(vatInvoice.getCompany())) {
                throw new ApiException("公司不能为空");
            }
            if (StringUtils.isEmpty(vatInvoice.getTaxSerial())) {
                throw new ApiException("税号不能为空");
            }
            if (StringUtils.isEmpty(vatInvoice.getAddress())) {
                throw new ApiException("地址不能为空");
            }
            if (StringUtils.isEmpty(vatInvoice.getBankName())) {
                throw new ApiException("银行不能为空");
            }
            if (StringUtils.isEmpty(vatInvoice.getAccount())) {
                throw new ApiException("账号不能为空");
            }
        }
        if (StringUtils.isNotEmpty(invoice.getUserId())) {
            if (!StringUtils.equals(invoice.getUserId(), userId)) {
                throw new ApiException("权限不够");
            }
        } else {
            invoice.setUserId(userId);
        }
        Invoice preInvoice = invoiceDao.findOne(Query.query(Criteria.where("userId").is(userId)));
        if (null == preInvoice) {
            invoiceDao.save(invoice);
        } else {
            invoice.setId(null);
            invoiceDao.updateSelective(preInvoice.getId(), invoice);
        }
        return invoice;
    }
    
	@Override
	public Invoice getById(String id) throws ApiException {
        try {
             return invoiceDao.findOne(Query.query(Criteria.where("id").is(id)));
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
	}
}
