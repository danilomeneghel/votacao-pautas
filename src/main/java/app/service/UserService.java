package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import app.model.User;

@ApplicationScoped
@Transactional(REQUIRED)
public class UserService {
	
	@Transactional()
    public List<User> findAllUsers() {
        return User.listAll();
    }

    @Transactional()
    public User findUser(Long id) {
        return User.findById(id);
    }
    
    @Transactional()
    public User findUserCpf(Long cpf) {
    	return User.find("cpf", cpf).firstResult();
    }

    public User insert(User user) {
    	user.persist();
        return user;
    }

    public User update(User loaded, User user) {
    	loaded.name = user.name;
    	loaded.email = user.email;
    	loaded.cpf = user.cpf;
    	loaded.username = user.username;
    	loaded.password = user.password;
    	loaded.role = user.role;
    	loaded.status = user.status;
    	loaded.persist();
        return loaded;
    }

    public void delete(Long id) {
        User user = User.findById(id);
        if(user!=null) {
            user.delete();
        }
    }
}
