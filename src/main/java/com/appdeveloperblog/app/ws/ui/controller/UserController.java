package com.appdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appdeveloperblog.app.ws.service.AddressService;
import com.appdeveloperblog.app.ws.service.UserService;
import com.appdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appdeveloperblog.app.ws.shared.dto.UserDto;
import com.appdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appdeveloperblog.app.ws.ui.model.response.UserRest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	 @Autowired
	 AddressService addressesService;
	 
	 @Autowired
	 AddressService addressService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE })
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();

		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;

	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		if (userDetails.getFirstName().isEmpty())
			throw new NullPointerException("The object is NULL");

		// UserDto userDto = new UserDto();
		// BeanUtils.copyProperties(userDetails, userDto);

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);

		return returnValue;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		userService.deleteUser(id);
		returnValue.setOperationName(RequestOperationName.Delete.name());
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		if (page > 0)
			page = page - 1;
		List<UserRest> returnValue = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

		return returnValue;
	}
 
	
	  @GetMapping(path="/{id}/addresses",produces =
	  {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE,"application/hal+json"})
	  public Resources<AddressesRest> getUserAddresses(@PathVariable String id) 
	  {
		   
	  List<AddressesRest> addressesListRestModel = new ArrayList<AddressesRest>();
	  
	  List<AddressDTO>  addressesDTO = addressesService.getAddresses(id);
	  
	  if(addressesDTO != null && !addressesDTO.isEmpty())
	  {
	  Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
	  addressesListRestModel = new ModelMapper().map(addressesDTO, listType);
		

	  for(AddressesRest addressRest : addressesListRestModel)
	  {
		  Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
		  addressRest.add(addressLink);
		  
		  Link userLink = linkTo(methodOn(UserController.class).getUser("id")).withRel("user");	  
		  addressRest.add(userLink);
		  
		  
	  }
	  
	  }
	  return new Resources<>(addressesListRestModel);
	  
	  }
	 
	  
	  
	  
	  @GetMapping(path="/{userId}/addresses/{addressId}",produces =
		  {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE,"application/hal+json"})
		  public Resource<AddressesRest> getUserAddress(@PathVariable String userId,@PathVariable String addressId) 
		  {
			   
			 
		  AddressDTO addressesDTO = addressService.getAddress(addressId);
		  	
		  Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		  Link userLink = linkTo((UserController.class)).slash(userId).withRel("user");
		  Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");

		  
		  AddressesRest addressesRestModel = new ModelMapper().map(addressesDTO, AddressesRest.class);
		  
		  addressesRestModel.add(addressLink);
		  addressesRestModel.add(userLink);
		  addressesRestModel.add(addressesLink);

		  return new Resource<>(addressesRestModel);
		  
		  }
	  
	  @GetMapping(path="/email-verification",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	  public OperationStatusModel verifyEmailToken(@RequestParam(value = "token")String token)
	  {
		  OperationStatusModel returnValue = new OperationStatusModel();
		  returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		  
		  boolean isVerified = userService.verifyEmailToken(token);
		  if(isVerified)
		  {
			  returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		  }
		  else
		  {
			  returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		  }
		  
		  return returnValue;
	  }
	  

}
