package com.example.miracle.user.service;

import com.example.miracle.email.ConfirmationTokenRepository;
import com.example.miracle.email.EmailSenderService;
import com.example.miracle.email.MailSender;
import com.example.miracle.exception.NotFoundException;
import com.example.miracle.image.dao.UserImageRepository;
import com.example.miracle.image.model.Image;
import com.example.miracle.image.model.ImageMapper;
import com.example.miracle.image.model.UserImage;
import com.example.miracle.user.dao.UserRepository;
import com.example.miracle.user.dto.UserDto;
import com.example.miracle.user.dto.UserMapper;
import com.example.miracle.user.model.Role;
import com.example.miracle.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${api.url}")
    private String apiUrl;
    @Value("${upload.path}")
    private String uploadPath;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserImageRepository userImageRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;
    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, UserImageRepository userImageRepository,
                           ConfirmationTokenRepository confirmationTokenRepository,
                           EmailSenderService emailSenderService, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userImageRepository = userImageRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailSenderService = emailSenderService;
        this.mailSender = mailSender;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto, MultipartFile file) throws IOException {
        User user = UserMapper.fromUserDto(userDto);
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (file != null) {
            Image image = ImageMapper.toImageUser(file);
            ImageMapper.linkImageToUser(image, user);

            UserImage userImage = saveFile(file);
            ImageMapper.linkUserImageToUser(userImage, user);
        }

        user.setActivationCode(UUID.randomUUID().toString());
        User savedUser = userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "<p>Hello, %s! \n" + "Welcome to Miracle. Please, visit next link:</p> <br> " +
                            "<a href=\" %sactivate/%s \"> Activate </a>",
                    user.getUsername(),
                    apiUrl,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }


        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public Page<UserDto> getAllUsers(Integer from, Integer size) {
        return userRepository.findAll(PageRequest.of(from / size, size))
                .map(UserMapper::toUserDto);
    }

    @Override
    public UserDto patchUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new NotFoundException("User not found!"));
        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        return UserMapper.toUserDto(user);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public void deleteUser(Integer userId) {
        UserImage userImage = userImageRepository.findByUserId(userId);
        File file = new File(userImage.getPath());
        if (file.delete()) {
            log.warn("File deleted!");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> search(String text, Integer from, Integer size) {
        return userRepository.search(text, PageRequest.of(from / size, size))
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }


    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @Override
    public UserImage saveFile(MultipartFile file) throws IOException {

        Path root = Paths.get(uploadPath);
        if (!Files.exists(root)) {
            init();
        }

        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = "" + System.currentTimeMillis() + ext;

        log.warn(fileName);

        Files.copy(file.getInputStream(), root.resolve(fileName));

        String path = uploadPath + "/" + fileName;

        return ImageMapper.toUserImage(file, path);

    }


    public UserDto activateUser(String code) {
        User user = userRepository.findByActivationCode(code).orElseThrow(() -> new NotFoundException("User is not found!"));
        user.setActivationCode(null);
        user.setEnabled(true);
        userRepository.save(user);

        return UserMapper.toUserDto(user);
    }
}
