package com.plumdo.form.rest.model.resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.form.domian.FormModel;
import com.plumdo.form.exception.ObjectNotFoundException;
import com.plumdo.form.repository.FormModelRepository;
import com.plumdo.form.rest.BaseResource;

public class BaseFormModelResource extends BaseResource {

	@Autowired
	protected FormModelRepository formModelRepository;

	protected FormModel getFormModelFromRequest(Long id) {
		FormModel formModel = formModelRepository.findOne(id);
		if (formModel == null) {
			throw new ObjectNotFoundException("Could not find a FormModel with id '" + id + "'.", FormModel.class);
		}
		return formModel;
	}

}
