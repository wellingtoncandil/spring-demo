package com.example.course.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.course.entities.User;
import com.example.course.repositories.IUserRepository;
import com.example.course.service.exceptions.DatabaseException;
import com.example.course.service.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	IUserRepository repository;

	public List<User> findAll() {
		return repository.findAll();
	}

	public Optional<User> findById(Long id) {
		Optional<User> obj = repository.findById(id);
		return Optional.of(obj.orElseThrow(() -> new ResourceNotFoundException(id)));
	}

	public User insert(User user) {
		return repository.save(user);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}
	
	public User update(Long id, User obj) {
		try {
		User user = repository.getById(id); //método getOne prepara o obj para ser inserido no bd sem que haja a necessidade de busca-lo previamente
		updateData(user, obj);
		return repository.save(user);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(User user, User obj) {
		user.setName(obj.getName());
		user.setEmail(obj.getEmail());
		user.setPhone(obj.getPhone());
	}
}
