import { Button, createTheme } from '@mui/material';
import styles from './Office.module.css'
import { Add } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import OfficeTable from '../components/officeTable/OfficeTable';
import Layout from '../../layout/Layout';

const Office = () => {
    const navigate = useNavigate();

    function add() {
        navigate('/offices/add')
    }
  return (
      <>
          <Layout>
          <div className={styles['screen-title']}>Offices</div>
          <div className={styles['button-top-right-above-card']}>
              <Button className={styles['card-button']} color="primary" variant="outlined"
                  onClick={add} startIcon={<Add></Add>}>add</Button>
          </div >
          <OfficeTable></OfficeTable>
              </Layout>
      </>  )
}

export default Office