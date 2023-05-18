import { useEffect, useState } from 'react'
import UserForm from '../../components/userForm/UserForm'
import styles from './CreateReadUpdateUser.module.css'
import { useLocation, useParams } from 'react-router'
import { GetUserByIdRequest, UserControllerApi, UserDto } from '../../../../shared/restApiClient'
import Layout from '../../../layout/Layout'

let readOnly;
let title;
let buttonText;
let cancelButtonText;

const CreateReadUpdateUser = () => {

    const userController = new UserControllerApi();
    const paramsLocation = useLocation();
    const paramsId = useParams();

    let [selectedUser, setSelectedUser] = useState<UserDto>();

    useEffect(() => {
        const api = async () => {
            if (paramsId.id !== undefined) {
                try {
                    const userId: GetUserByIdRequest = {
                        userId: +paramsId.id
                    }
                    const data = await userController.getUserById(userId);
                    setSelectedUser(data)
                } catch (error) {

                }

            } else {
                let user: UserDto = {
                    id: 0,
                    name: '',
                    firstName: '',
                    email: '',
                    role: '',
                    active: true
                }
                setSelectedUser(user)
            }
        }

        api();
    }, []);

    if (paramsLocation.pathname === '/users/add') {
        readOnly = false;
        title = 'Add user';
        buttonText = 'Add';
        cancelButtonText = 'Cancel'
    } else if (paramsLocation.pathname === '/users/edit/' + paramsId.id) {
        readOnly = false;
        title = 'Update user';
        buttonText = 'Update';
        cancelButtonText = 'Cancel'
    } else {
        readOnly = true;
        title = 'User details';
        buttonText = 'Ok';
        cancelButtonText = 'Return'
    }


  return (
    
      selectedUser ?
          <Layout>
              <UserForm readOnly={readOnly} title={title} buttonText={buttonText} cancelButtonText={cancelButtonText} selectedUser={selectedUser}></UserForm>    </Layout>
 : <div className={styles['screen-title']}>Loading ...</div>
  )
}

export default CreateReadUpdateUser