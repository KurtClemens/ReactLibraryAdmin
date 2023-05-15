import styles from './Requests.module.css'
import RequestTable from '../components/requestTable/RequestTable';


const Requests = () => {

  return (
    <>
      <div className={styles['screen-title']}>Requests</div>
      <RequestTable ></RequestTable>
    </>
  )
}

export default Requests