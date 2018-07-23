package com.hari.spb.react.service;

import java.util.List;

import com.hari.spb.react.entitybean.AssetMapping;
import com.hari.spb.react.entitybean.WorkOrderDetails;

public interface WorkOrderService {

	List<WorkOrderDetails> getWorkOrderDetailsByKKSId(String kksId);

	List<WorkOrderDetails> getWorkOrderDetailsByAssetId(String id);

	AssetMapping getAssetDetailsByKKSId(String id);

	List<AssetMapping> getKKSIdsByAssetId(String id);
	
	AssetMapping createAssetMapping(AssetMapping assetMapping);
	
	List<AssetMapping> getAllAssetMapping();
	
	boolean deleteAssetMapping(String kksId, String assetId);
	
	WorkOrderDetails createWorkOrderDetails(WorkOrderDetails WorkOrderDetails);
	
	WorkOrderDetails updateWorkOrderDetails(WorkOrderDetails WorkOrderDetails);
	
	WorkOrderDetails getWorkOrderDetails(long workOrder);
	
	boolean deleteWorkOrderDetails(long workOrder);

}
