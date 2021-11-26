package top.ahdx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.ahdx.mapper.UserInfoMapper;
import top.ahdx.mapper.UserMapper;
import top.ahdx.pojo.User;
import top.ahdx.pojo.UserInfo;
import top.ahdx.pojo.UserRole;
import top.ahdx.service.UserRoleService;
import top.ahdx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 遇见狂神说
 * @since 2020-06-28
 */
// UserDetailsService接口用于返回用户相关数据。
// 它有loadUserByUsername()方法，根据username查询用户实体，可以实现该接口覆盖该方法，实现自定义获取用户过程。
// 该接口实现类被DaoAuthenticationProvider 类使用，用于认证过程中载入用户信息。
@Service
@Transactional
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserRoleService roleService;
    @Autowired
    HttpSession session;


    // 用户登录逻辑和验证处理
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 通过用户名查询用户
        User user = userService.getOne(new QueryWrapper<User>().eq("username", s));

        // 放入session
        session.setAttribute("loginUser", user);

        //创建一个新的UserDetails对象，最后验证登陆的需要
        UserDetails userDetails = null;
        if (user != null) {
            //System.out.println("未加密："+user.getPassword());
            //String BCryptPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            // 登录后会将登录密码进行加密，然后比对数据库中的密码，数据库密码需要加密存储！
            String password = user.getPassword();

            //创建一个集合来存放权限
            Collection<GrantedAuthority> authorities = getAuthorities(user);
            //实例化UserDetails对象
            userDetails = new org.springframework.security.core.userdetails.User(s, password,
                    true,
                    true,
                    true,
                    true, authorities);
        }
        return userDetails;
    }

    // 获取角色信息
    private Collection<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        UserRole role = roleService.getById(user.getRoleId());
        //注意：这里每个权限前面都要加ROLE_。否在最后验证不会通过
        authList.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authList;
    }


    public void updateAvatar(String uid, String url) {
        User user = (User) session.getAttribute("loginUser");
        if (!user.getUid().equals(uid)) {
            throw new RuntimeException("小伙子不讲武德，耗子尾汁！");
        }
        user.setAvatar(url);
        try {
            baseMapper.update(user, new UpdateWrapper<User>().eq("uid", uid));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        user.setAvatar(url);
        session.setAttribute("loginUser", user);


    }


    @Override
    public List<UserInfo> findUserInfoByRolesIsNotAdmin() {
        return userInfoMapper.findUserInfoByRolesIsNotAdmin();
    }

    @Override
    public Integer deleteUser(String uid) {
        User user = userService.getOne(new QueryWrapper<User>().eq("uid", uid));
        if (user == null) return 0;

        if (1==user.getRoleId()) {
            return 1;
        }
        userInfoMapper.deleteById(uid);
        baseMapper.deleteById(user.getId());

        return -1;
    }
}
