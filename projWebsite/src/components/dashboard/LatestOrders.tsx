import { useState, useEffect } from 'react';
import moment from 'moment';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {
  Box,
  Button,
  Card,
  CardHeader,
  Chip,
  Divider,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TableSortLabel,
  TablePagination,
  Tooltip
} from '@material-ui/core';
import PurchaseService from "src/services/purchaseService";
import { toast } from 'react-toastify';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import Paginator from "src/components/paginator/Paginator";
interface Order {
  id: any;
  lat: number;
  lon: number;
  delivered: boolean;
  products: any;
  orderDate: any;
}

const notifySuccess = (msg) => {
  toast.success(msg, {
    position: toast.POSITION.TOP_CENTER
    });
}

const notifyError = (msg) => {
  toast.error(msg, {
    position: toast.POSITION.TOP_CENTER
    });
}

interface LatestOrdersProps {
  recent: string;
}

const LatestOrders: React.FC<LatestOrdersProps> = ({recent="asc"}) => {
  const [limit, setLimit] = useState(9);
  const [page, setPage] = useState(0);
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    PurchaseService.getPurchases(page, recent == 'desc')
      .then( (res) => {
        if (res.status === 200) {
          return res.json()
        }
        notifyError("Something went wrong")
        return null;
      })
      .then((res) => {
        console.log(res)
        if (res) {
          setOrders(res)
        }
      })
      .catch(() => {
        console.log("Something went wrong")
      })
  }, [recent])

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
    setPage(0);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  return (
    <Card>
      <CardHeader title="Latest Orders" />
      <Divider />
      <PerfectScrollbar>
        <Box sx={{ minWidth: 800 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell sortDirection={recent === 'asc'  ? 'asc' : 'desc'}>
                  Reference
                </TableCell>
                <TableCell>
                  Delivery Location
                </TableCell>
                <TableCell>
                  Order Date
                </TableCell>
                <TableCell>
                  Status
                </TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {orders && orders.slice(page*limit, page*limit + limit).map((order: Order, index) => (
                <TableRow
                  hover
                  key={order.id+recent}
                  style={{cursor:'pointer'}}
                  onClick={() => navigate('/app/order/'+order.id, { replace: true })}
                >
                  <TableCell>
                    {order.id}
                  </TableCell>
                  <TableCell>
                    <p>Latitude {order.lat}</p>
                    <p>Longitude {order.lon}</p>
                  </TableCell>
                  <TableCell>
                    {moment(order.orderDate).format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    <Chip
                      color="primary"
                      label={order.delivered ? "delivered" : "pending"}
                      size="small"
                    />
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Box>
      </PerfectScrollbar>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'flex-end',
          p: 2
        }}
      >
        <Paginator hasNext={orders.length == limit + 1} page={page} changePage={(page) => setPage(page)}/>
      </Box>
    </Card>
  )
}

export default LatestOrders;