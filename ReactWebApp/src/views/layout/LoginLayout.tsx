import { FC, ReactNode } from "react";
import { Box, CssBaseline } from "@mui/material";
import styles from './LoginLayout.module.css'
import LoginHeader from "../../components/loginHeader/LoginHeader";

interface LoginLayoutProps {
    children: ReactNode;
}


const LoginLayout:FC<LoginLayoutProps> = ({children}) => {
    return (
    <>
        <CssBaseline />
        <Box className={styles['layout-box']}>
            <LoginHeader />
            {children}
        </Box>
    </>
    );
};

export default LoginLayout