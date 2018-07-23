package com.hari.spb.react.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.hari.spb.react.dao.AssetMappingDao;
import com.hari.spb.react.dao.WorkOrderDao;
import com.hari.spb.react.entitybean.AssetMapping;
import com.hari.spb.react.entitybean.WorkOrderDetails;
import com.hari.spb.react.exception.NotFoundException;

@RunWith(SpringRunner.class)
public class WorkOrderServiceTest {
	
	@InjectMocks
	private WorkOrderService workOrderService = new WorkOrderServiceImpl();
	
	@MockBean
	private WorkOrderDao workOrderDao;
	
	@MockBean
	private AssetMappingDao assetMappingDao;
	
	@SuppressWarnings("unchecked")
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		MockUtil.setPrivateField(workOrderService, "workOrderDao", workOrderDao);
		MockUtil.setPrivateField(workOrderService, "assetMappingDao", assetMappingDao);
	}
	
	@Test
	public void testGetEmptyWorkOrderDetailsByKKSId() {
		AssetMapping assetMapping = createAssetMapping();
		Mockito.when(assetMappingDao.findByKksId(assetMapping.getKksId())).thenReturn(assetMapping);
		Mockito.when(workOrderDao.findAllByAssetId(assetMapping.getAssetId())).thenReturn(null);
		List<WorkOrderDetails> workOrderDetails = workOrderService.getWorkOrderDetailsByKKSId(assetMapping.getKksId());
		Assert.assertNull(workOrderDetails);
	}

	private AssetMapping createAssetMapping() {
		AssetMapping assetMapping = new AssetMapping();
		assetMapping.setAssetId("AS001");
		assetMapping.setKksId("KKS001");
		assetMapping.setUpdatedOn(new Date());
		assetMapping.setCreatedOn(new Date());
		return assetMapping;
	}

	@Test
	public void testGetWorkOrderDetailsByAssetId() {
		AssetMapping assetMapping = createAssetMapping();
		Mockito.when(workOrderDao.findAllByAssetId(assetMapping.getAssetId())).thenReturn(null);
		List<WorkOrderDetails> workOrderDetails = workOrderService.getWorkOrderDetailsByAssetId(assetMapping.getAssetId());
		Assert.assertNull(workOrderDetails);
	}

	@Test
	public void testGetAssetDetailsByKKSId() {
		AssetMapping assetMapping = createAssetMapping();
		Mockito.when(assetMappingDao.findByKksId(assetMapping.getKksId())).thenReturn(assetMapping);
		AssetMapping assetMappingResult = workOrderService.getAssetDetailsByKKSId(assetMapping.getKksId());
		Assert.assertEquals(assetMapping, assetMappingResult);
	}

	@Test(expected = NotFoundException.class)
	public void testGetKKSIdsByAssetId() {
		AssetMapping assetMapping = createAssetMapping();
		Mockito.when(assetMappingDao.findAllByAssetId(assetMapping.getAssetId())).thenReturn(new ArrayList<>());
		workOrderService.getKKSIdsByAssetId(assetMapping.getAssetId());
	}
	
	@Test
	public void testCreateAssetMapping() {
		AssetMapping assetMapping = createAssetMapping();
		Mockito.when(assetMappingDao.saveAndFlush(assetMapping)).thenReturn(assetMapping);
		AssetMapping assetMappingResult = workOrderService.createAssetMapping(assetMapping);
		Assert.assertEquals(assetMapping, assetMappingResult);
	}
}
