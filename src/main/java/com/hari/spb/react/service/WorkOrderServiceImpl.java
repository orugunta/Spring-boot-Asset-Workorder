package com.hari.spb.react.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hari.spb.react.dao.AssetMappingDao;
import com.hari.spb.react.dao.WorkOrderDao;
import com.hari.spb.react.entitybean.AssetMapping;
import com.hari.spb.react.entitybean.WorkOrderDetails;
import com.hari.spb.react.exception.AppErrors;
import com.hari.spb.react.exception.NotFoundException;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	@Autowired
	private WorkOrderDao workOrderDao;
	
	@Autowired
	private AssetMappingDao assetMappingDao;
	
	@Override
	public List<WorkOrderDetails> getWorkOrderDetailsByKKSId(String kksId) {
		AssetMapping assetMapping = getAssetDetailsByKKSId(kksId);
		if (assetMapping == null || 
				assetMapping.getAssetId() == null 
				|| StringUtils.isEmpty(assetMapping.getAssetId())) {
			throw new NotFoundException(AppErrors.DEFAULT_NOT_FOUND, kksId + " not found in the system.");
		}
		return getWorkOrdersByAssetId(assetMapping.getAssetId());
	}
	
	@Override
	public List<WorkOrderDetails> getWorkOrderDetailsByAssetId(String assetId) {
		return getWorkOrdersByAssetId(assetId);
	}
	
	@Override
	public List<AssetMapping> getAllAssetMapping() {
		return assetMappingDao.findAll();
	}

	@Override
	public AssetMapping getAssetDetailsByKKSId(String kksId) {
		AssetMapping assetMapping = assetMappingDao.findByKksId(kksId);
		
		if (assetMapping == null || 
				assetMapping.getAssetId() == null || 
				assetMapping.getKksId() == null) {
			throw new NotFoundException(AppErrors.ASSET_NOTFOUND, "KKS id : " + kksId + " not found in the system.");
		}
		
		return assetMapping;
	}

	@Override
	public List<AssetMapping> getKKSIdsByAssetId(String assetId) {
		List<AssetMapping> assetMappings = assetMappingDao.findAllByAssetId(assetId);
		if (assetMappings == null || assetMappings.isEmpty()) {
			throw new NotFoundException(AppErrors.ASSET_NOTFOUND, "Asset id : " + assetId + " not found in the system.");
		}
		return assetMappings;
	}
	
	@Override
	public AssetMapping createAssetMapping(AssetMapping assetMapping) {
		return assetMappingDao.saveAndFlush(assetMapping);
	}

	@Override
	public boolean deleteAssetMapping(String kksId, String assetId) {
		AssetMapping assetMapping = assetMappingDao.findByAssetIdAndKksId(kksId, assetId);
		assetMappingDao.delete(assetMapping);
		return true;
	}

	@Override
	public WorkOrderDetails createWorkOrderDetails(WorkOrderDetails workOrderDetails) {
		AssetMapping assetMapping = assetMappingDao.findById(workOrderDetails.getAssetMapping().getAssetId()).get();
		workOrderDetails.setAssetMapping(assetMapping);
		return workOrderDao.saveAndFlush(workOrderDetails);
	}

	@Override
	public WorkOrderDetails updateWorkOrderDetails(WorkOrderDetails workOrderDetails) {
		return workOrderDao.saveAndFlush(workOrderDetails);
	}

	@Override
	public WorkOrderDetails getWorkOrderDetails(long workOrder) {
		return workOrderDao.findById(workOrder).get();
	}

	@Override
	public boolean deleteWorkOrderDetails(long workOrder) {
		workOrderDao.deleteById(workOrder);
		return true;
	}
	
	private List<WorkOrderDetails> getWorkOrdersByAssetId(String assetId) {
		if (assetId != null && StringUtils.isNotEmpty(assetId)) {
			return workOrderDao.findAllByAssetId(assetId);
		}
		return null;
	}
}
