package app.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

import app.enumerator.RoleUserEnum;
import app.model.User;

@ApplicationScoped
@Transactional()
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
    public User findFirstUser() {
    	return User.find("1=1").firstResult();
    }
    
    @Transactional()
    public User findUsername(String username) {
    	return User.find("username", username).firstResult();
    }
    
    @Transactional()
    public User findUserCpf(Long cpf) {
    	return User.find("cpf", cpf).firstResult();
    }
    
    @Transactional()
    public User findUsernameCpf(User user) {
    	return User.find("username = '"+user.username+"' OR "+"cpf = "+user.cpf).firstResult();
    }

    public User insert(User user) {
    	user.persist();
        return user;
    }

    public User update(User loaded, User user) {
    	loaded.name = user.name;
    	loaded.cpf = user.cpf;
    	loaded.email = user.email;
    	loaded.username = user.username;
    	loaded.password = user.password;
    	loaded.role = (user.role == null) ? RoleUserEnum.ASSOC : user.role;
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
