import React from 'react'
import styles from './LoginHeader.module.css'
import logo from '../../assets/logo.png'

const LoginHeader: React.FC<{}> = () => {
    return (
        <nav className={styles.navbar}>
            <div className={styles['logo-container']}>
                <img className={styles['logo']} src={logo} alt="logo"></img>
                <h2 className={styles['logo-title']}>R-Library</h2>
            </div>
        </nav>
    )
}

export default LoginHeader