package com.plumdo.form.rest.instance.resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.form.domian.FormInstance;
import com.plumdo.form.exception.ObjectNotFoundException;
import com.plumdo.form.repository.FormInstanceRepository;
import com.plumdo.form.rest.BaseResource;

public class BaseFormInstanceResource extends BaseResource {

	@Autowired
	protected FormInstanceRepository formInstanceRepository;

	protected FormInstance getFormInstanceFromRequest(Long id) {
		FormInstance formInstance = formInstanceRepository.findOne(id);
		if (formInstance == null) {
			throw new ObjectNotFoundException("Could not find a FormInstance with id '" + id + "'.", FormInstance.class);
		}
		return formInstance;
	}

}
