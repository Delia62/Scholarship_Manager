package school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import school.model.auth.User;
import school.model.business.Student;
import school.payload.request.LoginRequest;
import school.payload.response.AuthResponse;
import school.repository.AuthenticationResponse;
import school.repository.UserRepository;
import school.service.CustomUserService;

import javax.servlet.http.HttpSession;

@CrossOrigin
@Controller
public class AuthController {

    public static String emailUserSession = null;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomUserService userService;

    @Autowired
    private UserRepository userRepository;


    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println("token: " + token);
        System.out.println("email: " + loginRequest.getUsername());
        System.out.println("password: " + loginRequest.getPassword());
        if (token != null) {
            System.out.println("token: " + token);
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User user, HttpSession session) {
        try {
            System.out.println("test--------------------------------------------");
            userService.checkUser(user);
            System.out.println("checked--------------------------------------------");
            session.setAttribute("user", user);
            System.out.println(user);
            return ResponseEntity.ok().body(new AuthenticationResponse("Redirect to /validation"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @ResponseBody
    @GetMapping("/api/index")
    public ResponseEntity<?> testIndex(){
        return ResponseEntity.status(HttpStatus.OK)
            .body("");
    }


    @ResponseBody
    @GetMapping("/api/profile")
    public ResponseEntity<Student> getProfile(Authentication authentication) {
        Student studentProfile = userService.getUserDetailsByUsername(authentication.getName());
        return ResponseEntity.ok(studentProfile);
    }
    @GetMapping("/aapi/api/profile")
    public ResponseEntity<?> testProfile() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("");
    }



    @ResponseBody
    @GetMapping("/api/docs")
    public ResponseEntity<?> testDocs() {
        return ResponseEntity.status(HttpStatus.OK)
                .body("");
    }


    /*@ResponseBody
    @GetMapping("/profile")
    public ModelAndView getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return new ModelAndView("profile.html").addObject("username", userDetails.getUsername());
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    /*@GetMapping("/index")
    public ModelAndView getHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return new ModelAndView("index.html").addObject("username", userDetails.getUsername());
        } else {
            return new ModelAndView("redirect:/login");
        }
    }*/


}
