package com.autoask.dao.impl.qr;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.qr.QRCodeDao;
import com.autoask.entity.mongo.qr.QRCode;
import org.springframework.stereotype.Repository;

/**
 * @author hyy
 * @create 2016-12-19 13:46
 */
@Repository
public class QRCodeDaoImpl extends BaseDaoImpl<QRCode, String> implements QRCodeDao {
}
