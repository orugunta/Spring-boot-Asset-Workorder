package com.hari.spb.react.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hari.spb.react.entitybean.AssetMapping;
import com.hari.spb.react.entitybean.WorkOrderDetails;
import com.hari.spb.react.exception.AppErrors;
import com.hari.spb.react.exception.NotFoundException;
import com.hari.spb.react.service.WorkOrderService;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkOrderController.class)
public class WorkOrderControllerTest {
	
	@MockBean
    private WorkOrderService workOrderService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

	@Test
    public void testContexLoads() throws Exception {
		Assert.assertNotNull(mockMvc);
    }
	
	@Test
    public void testServiceHealthCheck() throws Exception {
		String response = this.mockMvc.perform(get("/workorder/healthcheck")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		Assert.assertEquals("I am running...", response);
    }
	
	@Test
	public void testGetAllAssetMappingsEmptyResults() throws Exception {
		Mockito.when(workOrderService.getAllAssetMapping()).thenReturn(new ArrayList<AssetMapping>());
		String response = this.mockMvc.perform(get("/workorder/asset")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		List<AssetMapping> assetMappings = convertToListObject(response);
		Assert.assertNotNull(assetMappings);
		Assert.assertTrue(assetMappings.isEmpty());
	}
	
	@Test
	public void testGetAllAssetMappings() throws Exception {
		List<AssetMapping> testAssetMappings = createTestData();
		Mockito.when(workOrderService.getAllAssetMapping()).thenReturn(testAssetMappings);
		String response = this.mockMvc.perform(get("/workorder/asset")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		List<AssetMapping> assetMappingsResults = convertToListObject(response);
		Assert.assertNotNull(assetMappingsResults);
		Assert.assertTrue(!assetMappingsResults.isEmpty());
		Assert.assertTrue(testAssetMappings.get(0).equals(assetMappingsResults.get(0)));
	}
	
	@Test
	public void testGetAssetDetailsByKKSIdThrowsNotFoundException() throws Exception {
		Mockito.when(workOrderService.getAssetDetailsByKKSId(Mockito.anyString())).thenThrow(new NotFoundException(AppErrors.ASSET_NOTFOUND, "KKS id : 123 not found in the system."));
		String response = this.mockMvc.perform(get("/workorder/kks/kksid001/asset")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		Map<String, String> errorResponseBody = convertToMapObject(response);
		Assert.assertEquals("AS001", errorResponseBody.get("errorCode"));
		Assert.assertEquals("404: Not Found", errorResponseBody.get("statusCode"));
		Assert.assertEquals("Asset mapping not found. KKS id : 123 not found in the system.", errorResponseBody.get("errorMessage"));
	}
	
	@Test
	public void testGetAssetDetailsByKKSId() throws Exception {
		AssetMapping testAssetMapping = createTestData().get(0);
		Mockito.when(workOrderService.getAssetDetailsByKKSId(Mockito.anyString())).thenReturn(testAssetMapping);
		String response = this.mockMvc.perform(get("/workorder/kks/kksid001/asset")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		AssetMapping assetMapping = convertToObject(response, AssetMapping.class);
		Assert.assertEquals(testAssetMapping.getAssetId(), assetMapping.getAssetId());
		Assert.assertEquals(testAssetMapping.getKksId(), assetMapping.getKksId());
	}
	
	@Test
	public void testGetKKSIdsByAssetIdThrowsNotFoundException() throws Exception {
		Mockito.when(workOrderService.getKKSIdsByAssetId(Mockito.anyString())).thenThrow(new NotFoundException(AppErrors.ASSET_NOTFOUND, "Asset id : 321 not found in the system."));
		String response = this.mockMvc.perform(get("/workorder/asset/asset001/kks")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		Map<String, String> errorResponseBody = convertToMapObject(response);
		Assert.assertEquals("AS001", errorResponseBody.get("errorCode"));
		Assert.assertEquals("404: Not Found", errorResponseBody.get("statusCode"));
		Assert.assertEquals("Asset mapping not found. Asset id : 321 not found in the system.", errorResponseBody.get("errorMessage"));
	}
	
	@Test
	public void testGetKKSIdsByAssetId() throws Exception {
		List<AssetMapping> testAssetMappings = createTestData();
		Mockito.when(workOrderService.getKKSIdsByAssetId(Mockito.anyString())).thenReturn(testAssetMappings);
		String response = this.mockMvc.perform(get("/workorder/asset/asset001/kks")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		List<AssetMapping> assetMappings = convertToListObject(response);
		Assert.assertEquals(testAssetMappings.get(0).getAssetId(), assetMappings.get(0).getAssetId());
		Assert.assertEquals(testAssetMappings.get(0).getKksId(), assetMappings.get(0).getKksId());
	}
	
	@Test
	public void testCreateAssetMapping() throws Exception {
		AssetMapping testAssetMapping = createTestData().get(0);
		Mockito.when(workOrderService.createAssetMapping(Mockito.any(AssetMapping.class))).thenReturn(testAssetMapping);
		String response = this.mockMvc.perform(post("/workorder/asset/asset001/kks/kksid001")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		AssetMapping assetMapping = convertToObject(response, AssetMapping.class);
		Assert.assertEquals(testAssetMapping.getAssetId(), assetMapping.getAssetId());
		Assert.assertEquals(testAssetMapping.getKksId(), assetMapping.getKksId());
	}
	
	@Test
	public void testDeleteAssetMappingSuccess() throws Exception {
		Mockito.when(workOrderService.deleteAssetMapping(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		String response = this.mockMvc.perform(delete("/workorder/asset/asset001/kks/kksid001")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		Assert.assertEquals("true", response);
	}
	
	@Test
	public void testDeleteAssetMappingFailure() throws Exception {
		Mockito.when(workOrderService.deleteAssetMapping(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		String response = this.mockMvc.perform(delete("/workorder/asset/asset001/kks/kksid001")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		Assert.assertEquals("false", response);
	}
	
	@Test
	public void testCreateWorkOrder() throws Exception {
		WorkOrderDetails workOrderDetails = createWorkOrderDetails();
		Mockito.when(workOrderService.createWorkOrderDetails(Mockito.any(WorkOrderDetails.class))).thenReturn(workOrderDetails);
		String response = this.mockMvc.perform(post("/workorder/workorder").content(convertToString(workOrderDetails))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		WorkOrderDetails workOrderDetailsResponse = convertToObject(response, WorkOrderDetails.class);
		Assert.assertEquals(workOrderDetails.getAssetMapping().getAssetId(),
				workOrderDetailsResponse.getAssetMapping().getAssetId());
	}
	
	@Test
	public void testGetWorkOrder() throws Exception {
		WorkOrderDetails workOrderDetails = createWorkOrderDetails();
		Mockito.when(workOrderService.getWorkOrderDetails(Mockito.anyLong())).thenReturn(workOrderDetails);
		String response = this.mockMvc.perform(get("/workorder/workorder/4324325443")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		WorkOrderDetails workOrderDetailsResponse = convertToObject(response, WorkOrderDetails.class);
		Assert.assertEquals(workOrderDetails.getAssetMapping().getAssetId(),
				workOrderDetailsResponse.getAssetMapping().getAssetId());
	}
	
	@Test
	public void testUpdateWorkOrder() throws Exception {
		WorkOrderDetails workOrderDetails = createWorkOrderDetails();
		Mockito.when(workOrderService.updateWorkOrderDetails(Mockito.any(WorkOrderDetails.class))).thenReturn(workOrderDetails);
		String response = this.mockMvc.perform(put("/workorder/workorder/4324325443").content(convertToString(workOrderDetails))
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		WorkOrderDetails workOrderDetailsResponse = convertToObject(response, WorkOrderDetails.class);
		Assert.assertEquals(workOrderDetails.getAssetMapping().getAssetId(),
				workOrderDetailsResponse.getAssetMapping().getAssetId());
	}
	
	@Test
	public void testDeleteWorkOrder() throws Exception {
		Mockito.when(workOrderService.deleteWorkOrderDetails(Mockito.anyLong())).thenReturn(true);
		String response = this.mockMvc.perform(delete("/workorder/workorder/4324325443")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
		Assert.assertEquals("true", response);
	}
	
	private <T> String convertToString(T objectData) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(objectData);
	}
	
	private <T> T convertToObject(String jsonData, Class<T> klass) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return (T) objectMapper.readValue(jsonData, klass);
	}
	
	private List<AssetMapping> convertToListObject(String jsonData) throws JsonParseException, JsonMappingException, IOException {
		Gson gson = new Gson();
		Type type = new TypeToken<List<AssetMapping>>() {}.getType();
		return gson.fromJson(jsonData, type);
	}
	
	private Map<String, String> convertToMapObject(String jsonData) throws JsonParseException, JsonMappingException, IOException {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		return gson.fromJson(jsonData, type);
	}
	
	private List<AssetMapping> createTestData() {
		List<AssetMapping> assetMappings = new ArrayList<>();
		AssetMapping assetMapping = new AssetMapping();
		assetMapping.setAssetId("asset001");
		assetMapping.setCreatedOn(new Date());
		assetMapping.setKksId("kksid001");
		assetMapping.setUpdatedOn(new Date());
		assetMapping.setWorkOrderDetails(null);
		assetMappings.add(assetMapping);
		return assetMappings;
	}
	
	private WorkOrderDetails createWorkOrderDetails() {
		WorkOrderDetails workOrderDetails = new WorkOrderDetails();
		workOrderDetails.setActualCloseDate(new Date());
		workOrderDetails.setActualFinishDate(new Date());
		workOrderDetails.setActualStartDate(new Date());
		AssetMapping assetMapping = new AssetMapping();
		assetMapping.setAssetId("asset_id_1A1HLA80CP101");
		assetMapping.setCreatedOn(new Date());
		assetMapping.setKksId("1A1HLA80CP101");
		assetMapping.setUpdatedOn(new Date());
		
		workOrderDetails.setAssetMapping(assetMapping);
		workOrderDetails.setCause("Failure on the neck");
		workOrderDetails.setFailureClass("Failure on the neck");
		workOrderDetails.setPriority("High");
		workOrderDetails.setProblem("Pump Temperature issue");
		workOrderDetails.setRemedy("Testing");
		workOrderDetails.setReportedDate(""+new Date().getTime());
		workOrderDetails.setScheduledFinishDate(new Date());
		workOrderDetails.setScheduledStartDate(new Date());
		workOrderDetails.setTargetFinishDate(new Date());
		workOrderDetails.setTargetStartDate(new Date());
		workOrderDetails.setWoStatus("Open");
		workOrderDetails.setWoType("PumpType");
		return workOrderDetails;
	}
}
