import { Add } from '@mui/icons-material';
import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import styles from './Requests.module.css'
import RequestTable from '../components/requestTable/RequestTable';


const Requests = () => {
  const navigate = useNavigate();

  function add() {
    navigate('/requests/add')
  }


  return (
    <>
      <div className={styles['screen-title']}>Requests</div>
      <RequestTable ></RequestTable>
    </>
  )
}

export default Requests