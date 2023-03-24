import React, { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import {
  Button,
  ButtonGroup,
  Confirm,
  Divider,
  Grid,
  Icon,
  Input,
  Segment,
  Table
} from "semantic-ui-react";
import { EditMenu } from "../../../Components/EditMenu";
import MainMenu from "../../../Components/MainMenu";

const JSON_HEADERS = {
    "Content-Type": "application/json",
  };

  export function ViewHolidays() {
const [holidays,setHolidays]= useState([]);
const [active, setActive] = useState("");;
const [activePage, setActivePage] = useState(0);
const [nameText, setNameText] = useState("");
const [pagecount, setPageCount] = useState();
const [holidaysforPaging, setHolidaysForPaging] = useState([]);

const [open, setOpen] = useState(false);

const fetchHolidays = async () => {
  fetch(`/api/v1/holidays/page?page=` + activePage)
    .then((response) => response.json())
    .then((jsonResponse) => setHolidays(jsonResponse));  
};
const fetchFilterHolidays = async () => {
  fetch(`/api/v1/holidays/name-filter/${nameText}`)
    .then((response) => response.json())
    .then((jsonRespone) => setHolidays(jsonRespone));
};
const removeHoliday = (id) => {
    fetch("/api/v1/holidays/" + id, {
        method: "DELETE",
    })
      .then(fetchHolidays)
      .then(setOpen(false));
  };
  const fetchSingleHolidays = async () => {
    fetch("/api/v1/holidays/")
      .then((response) => response.json())
      .then((jsonResponse) => setHolidaysForPaging(jsonResponse))
      .then(setPageCount(Math.ceil(holidaysforPaging.length / 10)));
  };
  useEffect(() => {
    if (pagecount !== null) {
      fetchSingleHolidays();
    }
  }, [holidays]);

  useEffect(() => {
    if (nameText.length === 0) {
      fetchHolidays();
    } else {
       fetchFilterHolidays();
    }
  }, [activePage, nameText]);

  return (
    <div>
      <MainMenu />

      <Grid columns={2}>
        <Grid.Column width={2} id="main">
          <EditMenu />
        </Grid.Column>

        <Grid.Column textAlign="left" verticalAlign="top" width={13}>
          <Segment id="segment" color="teal">
            <div id="holidays">
              <Input
                className="controls1"
                value={nameText}
                onChange={(e) => setNameText(e.target.value)}
                placeholder="Filtruoti pagal Pavadinima"
              />
              <Button
                id="details"
                icon
                labelPosition="left"
                className="controls"
                as={NavLink}
                exact
                to="/create/holidays"
              >
                <Icon name="database" />
                Kurti naują
              </Button>
              <Divider horizontal hidden></Divider>
              <Table selectable>
                <Table.Header>
                  <Table.Row>
                    <Table.HeaderCell>Atostogų pavadinimas</Table.HeaderCell>
                    <Table.HeaderCell>Data nuo</Table.HeaderCell>
                    <Table.HeaderCell>Data iki</Table.HeaderCell>
                    <Table.HeaderCell>Veiksmai</Table.HeaderCell>
                  </Table.Row>
                </Table.Header>

                <Table.Body>
                  {holidays.map((holiday) => (
                    <Table.Row key={holiday.id}>
                      <Table.Cell>{holiday.name}</Table.Cell>
                      <Table.Cell>{holiday.startDate}</Table.Cell>
                      <Table.Cell>{holiday.endDate}</Table.Cell>

                      <Table.Cell collapsing>
                        <Button
                          id="icocolor"
                          href={"#/view/holidays/edit/" + holiday.id}
                          basic
                          compact
                          icon="eye"
                          title="Peržiūrėti"
                          onClick={() => setActive(holiday.id)}
                        ></Button>
                        <Button
                          id="icocolor"
                          basic
                          compact
                          title="Ištrinti"
                          icon="trash alternate"
                          onClick={() => setOpen(holiday.id)}
                        ></Button>
                        <Confirm
                          open={open}
                          header="Dėmesio!"
                          content="Ar tikrai norite Ištrinti?"
                          cancelButton="Grįžti atgal"
                          confirmButton="Taip"
                          onCancel={() => setOpen(false)}
                          onConfirm={() => removeHoliday(open)}
                          size="small"
                        />
                      </Table.Cell>
                    </Table.Row>
                  ))}
                </Table.Body>
              </Table>

              <Divider hidden></Divider>

              <ButtonGroup compact basic>
                <Button
                  title="Atgal"
                  onClick={() =>
                    setActivePage(activePage <= 0 ? activePage : activePage - 1)
                  }
                  icon
                >
                  <Icon name="arrow left" />{" "}
                </Button>
                {/* {[...Array(pagecount)].map((e, i) => {
                  return (
                    <Button
                      title={i + 1}
                      key={i}
                      active={activePage === i ? true : false}
                      onClick={() => setActivePage(i)}
                    >
                      {i + 1}
                    </Button>
                  );
                })} */}
                <Button
                  title="Pirmyn"
                  onClick={() =>
                    setActivePage(
                      activePage >= pagecount - 1 ? activePage : activePage + 1
                    )
                  }
                  icon
                >
                  <Icon name="arrow right" />{" "}
                </Button>
              </ButtonGroup>
            </div>
          </Segment>
        </Grid.Column>
      </Grid>
    </div>
  );
  }