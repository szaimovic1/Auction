import { useState, useEffect } from 'react'
import { SERVER_USERS, SERVER_USER_REACTIVATE, SERVER_USER_DELETE,
    SERVER_USER_EXPORT, SERVER_USER_BLOCK } from '@endpoints'
import { useNavigate } from 'react-router'
import { LOGOUT, MSG_WIN, MSG_ERROR } from '@constants'
import { getData, putData, deleteData } from "@api/api";
import './users.scss';

const Users = ({ setMessage, style }) => {
    const [users, setUsers] = useState([])
    const [page, setPage] = useState(0)
    const [search, setSearch] = useState("")
    const [numberOfUsers, setNumOfUsers] = useState(0)
    const navigate = useNavigate()
    const buttonSellerNormal = "button-seller-normal"
    const [refresh, setRefresh] = useState(false)

    useEffect(() => {
        async function fetchData() {
            const params = {
                page: page,
                users: 5,
                search: search
            }
            const data = await getData(SERVER_USERS, params)
            if(data.logout) {
                navigate(LOGOUT)
            }
            if(data.isSuccess) {
                if(page === 0) {
                    setUsers(data.users)
                }
                else {
                    setUsers(u =>  u.concat(data.users))
                }
                setNumOfUsers(data.numberOfUsers)
            }
        }

        fetchData()
    }, [page, search, navigate, refresh])

    const reactivateUser = async (id) => {
        const data = await putData(SERVER_USER_REACTIVATE, 
          null, {userId: id})
        checkData(data)
    }

    const deleteUser = async (id) => {
        const data = await deleteData(SERVER_USER_DELETE, 
          { userId: id })
        if(data.logout) {
            return
        }
        if(data.isSuccess) {
            setPage(0)
            setRefresh(!refresh)
        }
    }

    const exportUser = async (id) => {
        const data = await getData(SERVER_USER_EXPORT, 
          { userId: id })
        checkData(data)
    }

    const blockUser = async (id) => {
      const data = await putData(SERVER_USER_BLOCK, 
        null, { userId: id })
      checkData(data)
    }

    const checkData = (data) => {
      if(data.logout) {
        return
      }
      if(data.isSuccess) {
          style.current = MSG_WIN
      }
      else {
          style.current = MSG_ERROR
      }
      setMessage(data.message)
    }

  return (
    <div>
      <table className="table-container-users">
        <tbody>
          <tr>
            <td className="table-users-col1">User</td>
            <td className="table-users-col2">
              <input className="input-search-users"
                  type="text" 
                  placeholder="Name/email" 
                  aria-label="Search" 
                  onChange={(e) => {setSearch(e.target.value)}}
              />
            </td>
          </tr>
        </tbody>
        {
          users.map((value, index) => {
            return (
              <tbody key={index}>
                <tr>
                  <td className="img-name" style={{marginLeft: "25%"}} >
                    <div className="img-bidders-container">
                      <img className="img-bidders" src={value.profilePicture} alt="" />
                    </div>
                    <div className="img-name-bidder">
                      {value.firstName + " " + value.lastName}
                    </div>
                  </td>
                  <td>
                    <div>
                      <button 
                        className={buttonSellerNormal}
                        onClick={() => { reactivateUser(value.id) }}
                      >
                          Reactivate
                      </button>
                      <button 
                        className={buttonSellerNormal}
                        onClick={() => { deleteUser(value.id) }}
                      >
                          Delete
                      </button>
                      <button 
                        className={buttonSellerNormal}
                        onClick={() => { exportUser(value.id) }}
                      >
                          Export
                      </button>
                      <button 
                        className={buttonSellerNormal}
                        onClick={() => { blockUser(value.id) }}
                      >
                          Block
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>);
        })}
        </table>
        {users.length < numberOfUsers && 
          <button 
            className="more-bidders" 
            onClick={() => {setPage(page+1)}}
          >+
          </button>
        }
    </div>
  )
}

export default Users
