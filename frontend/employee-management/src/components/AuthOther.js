import { Box, Button } from '@mui/material';
import React from 'react'
import { useNavigate } from 'react-router-dom';

function AuthOther() {
    const navigate = useNavigate();
    const loginGoogle = () => {
        const width = 500; // Popup width
        const height = 600; // Popup height
    
        // Calculate the center position
        const left = (window.screen.width - width) / 2;
        const top = (window.screen.height - height) / 2;
    
        // Open the popup window
        const popup = window.open(
          "http://localhost:8080/oauth2/authorization/google",
          "Google Login",
          `width=${width},height=${height},left=${left},top=${top}`
        );
    
        // Polling to check if the popup is closed
        const checkPopup = setInterval(() => {
          if (popup.closed) {
            clearInterval(checkPopup);
            navigate("/employee-list");
          }
        }, 500);
      };
    const loginGithub = () => {
        const width = 500; // Popup width
        const height = 600; // Popup height
    
        // Calculate the center position
        const left = (window.screen.width - width) / 2;
        const top = (window.screen.height - height) / 2;
    
        // Open the popup window
        const popup = window.open(
          "http://localhost:8080/oauth2/authorization/github",
          "Google Login",
          `width=${width},height=${height},left=${left},top=${top}`
        );
    
        // Polling to check if the popup is closed
        const checkPopup = setInterval(() => {
          if (popup.closed) {
            clearInterval(checkPopup);
            navigate("/employee-list");
          }
        }, 500);
      };
  return (
    <Box display={"flex"} gap={5}>
        <Button
          fullWidth
          variant="contained"
          sx={{ mt: 2 }}
          color="success"
          onClick={() => loginGoogle()}
        >
          Google
        </Button>
        <Button
          fullWidth
          variant="contained"
          sx={{ mt: 2 }}
          color="success"
          onClick={() => loginGithub()}
        >
          Github
        </Button>
      </Box>
  )
}

export default AuthOther;