import React from 'react';
import styles from './NavBar.module.css'
import * as data from './links.json'
import logo from '../../assets/logo.png'
import { Link } from '@mui/material';

const linksString = JSON.stringify(data);
const links = JSON.parse(linksString).links

type NavBarLink={
    label:string;
    href:string;
}

const NavBar: React.FC<{}> = () => {
  return (
    <nav className={styles.navbar}>
        <div className={styles['logo-container']}>
            <img className={styles['logo']} src={logo} alt="logo"></img>
            <h2 className={styles['logo-title']}>R-Library</h2>
        </div>
        <div className={styles['links-container']}>
              {links.map((link: NavBarLink)=>{
                return(
                    <div key={link.href} className={styles['link']}>
                        <Link href={link.href}>
                            {link.label}
                            </Link>
                    </div>
                )
            })}
        </div>
    </nav>
  )
}



export default NavBar