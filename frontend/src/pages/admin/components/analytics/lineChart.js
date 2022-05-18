import { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid,
   Tooltip, ResponsiveContainer, Label } from 'recharts';
import { getData } from "@api/api";
import { SERVER_USER_LOGIN } from "@endpoints";
import { LOGOUT } from "@constants";
import { useNavigate } from 'react-router';

const LineChartComponent = () => {
    const [data, setData] = useState()
    const navigate = useNavigate()

    useEffect(() => {
      async function fetchData() {
        const response = await getData(SERVER_USER_LOGIN)
        if(response.logout) {
          navigate(LOGOUT)
        }
        if(response.isSuccess) {
          setData([
            {
              time: 'Last week',
              users: response.lastWeek,
            },
            {
              time: "Last month",
              users: response.lastMonth,
            },
            {
              time: "Sometime before last month",
              users: response.beforeLastMonth,
            },
            {
              time: "Logged in just once",
              users: response.justOnce,
            },
          ])
        }
      }

      fetchData()
    }, [navigate])

  return (
    <div style={{width: "100%", marginTop: "20px"}}>
      {data &&
        <>  
          <h2>Users login activity over the period of time</h2>
          <ResponsiveContainer width="90%" aspect={3}>
          <LineChart
            width={500}
            height={300}
            data={data}
            margin={{
              top: 15,
              right: 30,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid  horizontal="true" vertical="" stroke="#E3E3E3"/>
            <XAxis dataKey="time" />
            <YAxis> 
              <Label 
                angle={-90} 
                value='Number of users' 
                position='insideLeft' 
                style={{textAnchor: 'middle' }} 
                fill="rgb(102, 102, 102)" 
              /> 
            </YAxis>
            <Tooltip
              contentStyle={{ backgroundColor: "#8884d8", color: "#fff" }} 
              itemStyle={{ color: "#fff" }} 
              cursor={false}
            />
            <Line 
              type="monotone" 
              dataKey="users" 
              stroke="#8884d8" 
              strokeWidth="5" 
              dot={{fill:"#2e4355",stroke:"#8884d8",strokeWidth: 2,r:5}} 
              activeDot={{fill:"#2e4355",stroke:"#8884d8",strokeWidth: 5,r:10}} 
            />
            
          </LineChart>
          </ResponsiveContainer>
        </>
      }
    </div>
  )
}

export default LineChartComponent
