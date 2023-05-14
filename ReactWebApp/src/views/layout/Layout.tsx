import { FC, ReactNode } from "react";
import { Box, CssBaseline } from "@mui/material";
import NavBar from "../../components/navbar/NavBar";
import styles from './Layout.module.css'

interface LayoutProps {
    children: ReactNode;
}

const Layout: FC<LayoutProps> = ({ children }) => {
    return (
        <>
            <CssBaseline />
            <Box className={styles['layout-box']}>
                <NavBar />
                {children}
            </Box>
        </>
    );
};

export default Layout;