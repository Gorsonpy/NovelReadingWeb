package idi.gorsonpy.auth.realm;

import idi.gorsonpy.domain.User;
import idi.gorsonpy.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;


    //权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.selectUserByUserName(userName);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user.isAdmin()) { //管理员有额外的功能
            info.addRole("admin");
        } else {
            info.addRole("general");
        }
        return info;
    }


    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        System.out.println(username);
        User user = userService.selectUserByUserName(username);
        String password = new String((char[]) authenticationToken.getCredentials());
        System.out.println(user);
        if (user == null) {
            System.out.println("当前认证的用户不存在");
            throw new UnknownAccountException();
        } else if (!user.getPassword().equals(password)) {
            System.out.println("密码错误");
            throw new UnknownAccountException();
        }

        System.out.println("验证成功");
        ByteSource salt = ByteSource.Util.bytes(username);

        //token存放的也是加盐加密后的密码
        return new SimpleAuthenticationInfo(user, password, salt, this.getName());
    }

}
