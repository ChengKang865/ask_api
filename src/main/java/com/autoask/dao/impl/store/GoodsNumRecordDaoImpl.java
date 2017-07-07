package com.autoask.dao.impl.store;

import com.autoask.dao.impl.BaseDaoImpl;
import com.autoask.dao.store.GoodsNumRecordDao;
import com.autoask.entity.mongo.store.GoodsNumRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by hyy on 2016/12/3.
 */
@Repository
public class GoodsNumRecordDaoImpl extends BaseDaoImpl<GoodsNumRecord, String> implements GoodsNumRecordDao {
}
