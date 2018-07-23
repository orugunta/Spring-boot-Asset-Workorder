package com.hari.spb.react.controller;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hari.spb.react.entitybean.AssetMapping;
import com.hari.spb.react.entitybean.WorkOrderDetails;
import com.hari.spb.react.exception.BadRequestException;
import com.hari.spb.react.exception.AppErrors;
import com.hari.spb.react.service.WorkOrderService;

@CrossOrigin
@RestController
@RequestMapping(value = "/workorder")
public class WorkOrderController extends AbstractController {

private static final Logger logger = LoggerFactory.getLogger(WorkOrderController.class);
	
	@Autowired
	private WorkOrderService workOrderService;

	@GetMapping(value = "/healthcheck")
	public String healthCheck() {
		logger.debug("WorkOrderController.healthCheck()--->inside");
		return "I am running...";
	}
	
	@GetMapping(value = "/asset")
	public List<AssetMapping> getAllAssetDetails() {
		logger.debug("WorkOrderController.getAllAssetDetails()--->inside");
		return workOrderService.getAllAssetMapping();
	}
	
	@GetMapping(value = "/kks/{id}/asset")
	public AssetMapping getAssetDetailsByKKSId(@PathVariable String id) {
		logger.debug("WorkOrderController.getWorkOrderDetailsByKKSId()--->inside");
		validateAssetInputData(id);
		return workOrderService.getAssetDetailsByKKSId(id);
	}
	
	@GetMapping(value = "/asset/{id}/kks")
	public List<AssetMapping> getKKSIdsByAssetId(@PathVariable String id) {
		logger.debug("WorkOrderController.getWorkOrderDetailsByKKSId()--->inside");
		validateAssetInputData(id);
		return workOrderService.getKKSIdsByAssetId(id);
	}

	@PostMapping(value = "/asset/{assetId}/kks/{kksId}")
	public AssetMapping createAssetMapping(@PathVariable String assetId, @PathVariable String kksId) {
		logger.debug("WorkOrderController.createAssetMapping(assetId = {} kksId = {})", assetId, kksId);
		validateAssetInputData(assetId);
		validateAssetInputData(kksId);
		AssetMapping assetMapping = new AssetMapping();
		assetMapping.setAssetId(assetId);
		assetMapping.setKksId(kksId);
		assetMapping.setCreatedOn(new Date());
		assetMapping.setUpdatedOn(new Date());
		return workOrderService.createAssetMapping(assetMapping);
	}
	
	@DeleteMapping(value = "/asset/{assetId}/kks/{kksId}")
	public boolean deleteAssetMapping(@PathVariable String assetId, @PathVariable String kksId) {
		logger.debug("WorkOrderController.deleteAssetMapping(assetId = {} kksId = {})", assetId, kksId);
		validateAssetInputData(assetId);
		validateAssetInputData(kksId);
		return workOrderService.deleteAssetMapping(kksId, assetId);
	}
	
	@GetMapping(value = "/kks/{id}/workorder")
	public List<WorkOrderDetails> getWorkOrderDetailsByKKSId(@PathVariable String id) {
		logger.debug("WorkOrderController.getWorkOrderDetailsByKKSId()--->inside");
		validateAssetInputData(id);
		List<WorkOrderDetails> workOrderDetails = workOrderService.getWorkOrderDetailsByKKSId(id);
		logger.debug("WorkOrderController.getWorkOrderDetailsByKKSId(results size {})--->", workOrderDetails.size());
		return workOrderDetails;
	}
	
	@GetMapping(value = "/asset/{id}/workorder")
	public List<WorkOrderDetails> getWorkOrderDetailsByAssetId(@PathVariable String id) {
		logger.debug("WorkOrderController.getWorkOrderDetailsByAssetId()--->inside");
		validateAssetInputData(id);
		List<WorkOrderDetails> workOrderDetails = workOrderService.getWorkOrderDetailsByAssetId(id);
		logger.debug("WorkOrderController.getWorkOrderDetailsByAssetId(results size {})--->", workOrderDetails.size());
		return workOrderDetails;
	}
	
	@GetMapping(value = "/workorder/{workOrderId}")
	public WorkOrderDetails getWorkOrder(@PathVariable String workOrderId) {
		logger.debug("WorkOrderController.getWorkOrder(workOrderId = {})", workOrderId);
		validateWorkOrderInputData(workOrderId);
		return workOrderService.getWorkOrderDetails(Long.parseLong(workOrderId));
	}
	
	@PostMapping(value = "/workorder")
	public WorkOrderDetails createWorkOrder(@RequestBody WorkOrderDetails WorkOrderDetails) {
		logger.debug("WorkOrderController.createWorkOrder(WorkOrderDetails = {})", WorkOrderDetails.toString());
		return workOrderService.createWorkOrderDetails(WorkOrderDetails);
	}
	
	@PutMapping(value = "/workorder/{workOrderId}")
	public WorkOrderDetails updateWorkOrder(@PathVariable String workOrderId, @RequestBody WorkOrderDetails WorkOrderDetails) {
		logger.debug("WorkOrderController.updateWorkOrder(workOrderId = {})", workOrderId);
		validateWorkOrderInputData(workOrderId);
		WorkOrderDetails.setWorkOrderId(Long.parseLong(workOrderId));
		return workOrderService.updateWorkOrderDetails(WorkOrderDetails);
	}
	
	@DeleteMapping(value = "/workorder/{workOrderId}")
	public boolean deleteWorkOrder(@PathVariable String workOrderId) {
		logger.debug("WorkOrderController.deleteWorkOrder(workOrderId = {})", workOrderId);
		validateWorkOrderInputData(workOrderId);
		return workOrderService.deleteWorkOrderDetails(Long.parseLong(workOrderId));
	}
	
	private void validateAssetInputData(String id) {
		if (id == null || StringUtils.isEmpty(id)) {
			throw new BadRequestException(AppErrors.ASSET_BAD_REQUEST, "Input data can't be null or empty");
		}
	}
	
	private void validateWorkOrderInputData(String workOrderId) {
		if (workOrderId == null || StringUtils.isEmpty(workOrderId) || !StringUtils.isNumeric(workOrderId) ) {
			throw new BadRequestException(AppErrors.WORK_ORDER_BAD_REQUEST, "workorder id should be number.");
		}
	}
}
